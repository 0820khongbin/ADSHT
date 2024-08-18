<template>
  <div class="container">
    <div class="search-section">
      <input type="text" placeholder="Search shop" v-model="searchQuery" @input="doInputKey($event.target.value)" class="search-input"
        id="searchShop">
      <select class="filter-select" @input="doInput($event.target.value)">
        <option value="shop">shop</option>
        <option value="product">product</option>
        <!-- Add more filter options as needed -->
      </select>
      <!--查詢訂單-->
      <router-link to="/views/orderlist">
        <button class="btn btn-light"><font-awesome-icon :icon="['fas', 'list']" />查看訂單</button>
      </router-link>
    </div>
    <div class="shop-grid">
      <Shop v-if="searchThing == 'shop'" v-for="(shop, index) in res" :key="index" :shop="shop"></Shop>
      <ProductComp v-else v-for="(product, index) in res" :product="product"></ProductComp>
    </div>
    <div>
      <button v-show="show" v-if="ownShopId == 0" type="button" class="btn btn-primary post-button" data-bs-toggle="modal"
        data-bs-target="#postModal">
        Create Your Shop
      </button>
      <RouterLink v-else :to="{ name: 'david-shop-link', query: { id: ownShopId } }" class="btn btn-primary post-button">
        YourShop
      </RouterLink>
    </div>
    <ShopModal></ShopModal>
  </div>
  <!-- IntersectionObserver 觀察目標  -->
  <div class="observer-target" ref="observerTarget"></div>
</template>

<script setup>
import ProductComp from '@/components/david-comp/ProductComp.vue';
import ShopModal from '@/components/david-comp/ShopCreateModal.vue';
import Shop from '@/components/david-comp/ShopSearchComp.vue';
import axiosapi from '@/plugins/axios';
import { onBeforeUnmount, onMounted, ref } from 'vue';
const ownShopId = ref(0)
const show = ref(true)
const res = ref([]);
const searchThing = ref('shop')
//無限讀取用
const page = ref(1); // Current page of data
const perPage = ref(10); // Number of items per page
const maxPages = ref(50); // Maximum number of pages
const loading = ref(true); // Loading state
const newCards = ref([])
// Function to fetch shops
const searchKey = ref('');
const fetchShops = async () => {
  try {
    let sessionId = sessionStorage.getItem("Session-UserInfoId") || 0;
    axiosapi.defaults.headers["session-user-info-id"] = sessionId;
    const response = await axiosapi.post("/ajax/shop/shop", {"page": page.value}).then((response) => {
      res.value = response.data.shops;
      if (response.data.ownShop) {
        ownShopId.value = response.data.ownShop;
      }
      if (response.data.user) {
        show.value = false;
      }

    });
  } catch (error) {
    console.error('Failed to fetch shops:', error); // Handle error
  }

  if (res.value.length > 11){
        loading.value = false;
      }else{
        loading.value = true;
      }
};

function doInputKey(value){
  searchKey.value = value
  page.value = 1;
  searchShop();
}

function searchShop() {
  let request = {
      content: searchKey.value,
      order: "",
      class: "",
      page: "",
      showNum: "",
      page:page.value,
    }
    console.log(request);
  if (searchThing.value == "shop") {
    axiosapi.post('ajax/shop/findShops', request).then((response) => {
      if (page.value == 1){
        res.value = response.data.shop;
      }else{
        newCards.value = response.data.shop;
      }
    })
  } else {
    axiosapi.post('ajax/shop/findProducts', request).then((response) => {
      if (page.value == 1){
        res.value = response.data.products;
      }else{
        newCards.value = response.data.products;
      }
    })
  }

  //設定自動讀取偵測
  if (res.value.length > 11){
        
        loading.value = false;
      }else{
        loading.value = true;
      }

}

function doInput(thing) {
  searchThing.value = thing
}

//無限讀取
const loadMoreCards = async () => {
  if (page.value <= maxPages.value && !loading.value) {
    page.value++;
    loading.value = true; // Set loading state to true
    await searchShop(searchKey.value); // Fetch new cards
    res.value = [...res.value, ...newCards.value]; // Append new cards
    // Increment page number
    loading.value = false; // Set loading state back to false
  }
};

const observerCallback = (entries) => {
  entries.forEach((entry) => {
    if (entry.isIntersecting) {
      loadMoreCards(); // Load more cards when entry is intersecting
    }
  });
};

const observerOptions = {
  root: null, // Use the viewport as the root
  rootMargin: '0px',
  threshold: 0.5,
};

const observer = new IntersectionObserver(observerCallback, observerOptions);

// Fetch shops when component is mounted
onMounted(() => {
  fetchShops();
  document.getElementById('searchShop').addEventListener('input', searchShop)
  const observerTarget = document.querySelector('.observer-target');
  if (observerTarget) {
    observer.observe(observerTarget);
  }
});
onBeforeUnmount(() => {
  // Stop observing and clean up
  observer.disconnect();
});
</script>
<style>
/* Container for the whole page */
.container {
  padding: 20px;
}

/* Search and filter section */
.search-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.search-input {
  width: 70%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.filter-select {
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

/* Grid layout for shop items */
.shop-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 20px;
}

/* Style for the floating action button */
.post-button {
  position: fixed;
  right: 20px;
  bottom: 20px;
  padding: 10px 20px;
  font-size: 16px;
  border-radius: 50px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

/* Adjust button styles */
.btn-primary {
  background-color: #007bff;
  color: #fff;
  border: none;
  transition: background-color 0.3s;
}

.btn-primary:hover {
  background-color: #0056b3;
}
</style>