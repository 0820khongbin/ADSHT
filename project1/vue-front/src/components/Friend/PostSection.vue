<template>
    <div>
      <div v-if="dataLoaded && noPosts" class="noPost-Shop">
        <font-awesome-icon icon="fa-solid fa-camera" class="large-size-icon" />
        <h1 class="noContent">尚無貼文</h1>
      </div>
      <div v-else-if="dataLoaded" class="row">
        <div v-for="post in posts" :key="post.postId" class="imageForPost">
          <img v-lazy="post.image || defaultImage" alt="">
          <div class="details" @click="openImageModal(post)">
            <h2><span></span></h2>
            <p><font-awesome-icon icon="fa-regular fa-eye" /></p>
            <div class="more">
              <a class="read-more">{{ formatDate(post.addTime) }}</a>
              <div class="icon-links"></div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    props: {
      posts: Array,
      dataLoaded: Boolean,
      noPosts: Boolean,
      defaultImage: String,
    },
    methods: {
      formatDate(dateString) {
        const date = new Date(dateString);
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        return `${year}年${month}月${day}日`;
      },
      openImageModal(post) {
        this.$emit('open-image-modal', post);
      },
    },
  };
  </script>
  
  <style scoped>
    @import '@/assets/personalPage.css';
    @import '@/assets/personalUpdate.css';
    @import '@/assets/personalPost.css';
  </style>
  