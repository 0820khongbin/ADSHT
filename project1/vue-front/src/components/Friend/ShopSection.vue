<template>
    <div>
      <div v-if="shopDataLoaded && noShops" class="noPost-Shop">
        <font-awesome-icon icon="fa-solid fa-shop-slash" class="large-size-icon" />
        <h1 class="noContent">尚無商品</h1>
      </div>
      <div v-else-if="shopDataLoaded" class="row">
        <div v-for="shop in shops" :key="shop.shopId" class="shop-card">
          <div v-for="product in shop.products" :key="product.productId" class="product-card">
            <img :src="product.productImages[0]?.image || defaultImage" alt="Product Image" style="object-fit: contain; width: 100%; background-color: black;">
            <div class="product-details">
              <h4>{{ product.productName }}</h4>
              <p>商品描述:{{ product.productDescribe }}</p>
              <div class="product-buy">
                <div><font-awesome-icon icon="fa-solid fa-dollar-sign" /> {{ product.productPrice }}</div>
                <button class="product-cart" @click="addToCart(product.productId, product.productPrice)">
                  <font-awesome-icon icon="fa-solid fa-bag-shopping" />
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue';
  import axiosapi from '@/plugins/axios';
  import Swal from 'sweetalert2';
  
  const props = defineProps({
    shops: Array,
    shopDataLoaded: Boolean,
    noShops: Boolean,
    defaultImage: String,
  });
  
  const addToCart = async (productId, price) => {
    let storedProduct = sessionStorage.getItem('products');
    let productSession = storedProduct ? JSON.parse(storedProduct) : {};
  
    if (!productSession[productId]) {
      productSession[productId] = { value: 1, price: price };
      sessionStorage.setItem('products', JSON.stringify(productSession));
      axiosapi.defaults.headers["session-id"] = sessionStorage.getItem("Session-Id");
      try {
        const response = await axiosapi.post(`/ajax/shop/addToCart/${productId}`);
        if (response.data.success) {
          Swal.fire({
            icon: "success",
            title: "商品已加入購物車",
            text: response.data.message,
            timer: 1000,
            showConfirmButton: false
          }).then(() => {
            window.location.reload();
          });
        } else {
          Swal.fire({
            icon: "error",
            title: "添加到購物車失敗",
            text: response.data.message,
            timer: 1000,
            showConfirmButton: false
          });
        }
      } catch (error) {
        console.error('Error adding to cart:', error);
      }
    } else {
      Swal.fire({
        icon: "info",
        title: "商品已在購物車中",
        text: "您已經將此商品加入購物車。",
        timer: 1000,
        showConfirmButton: false
      });
    }
  };
  </script>
  
  <style scoped>
    @import '@/assets/personalPage.css';
    @import '@/assets/personalUpdate.css';
    @import '@/assets/personalPost.css';
  </style>
  