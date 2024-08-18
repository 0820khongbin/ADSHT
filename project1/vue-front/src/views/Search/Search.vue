<template>
  <div class="centered-container">
    <SearchComment :searchQuery="searchQuery" :searchResults="searchResults" :searchPostRes="searchPostRes"
      @update:selectedTab="selectedTab = $event" @update:searchQuery="searchQuery = $event" @toggleFollow="toggleFollow"
      @searchUser="searchUser" @posts="posts" />
    
  </div>
  <!-- IntersectionObserver 觀察目標  -->
  <div class="observer-target" ref="observerTarget"></div>
</template>

<script setup>
import SearchComment from "@/components/Search/SearchComment.vue";
import axiosapi from "@/plugins/axios";
import { onBeforeUnmount, onMounted, ref } from 'vue';

const selectedTab = ref('posts');
const searchQuery = ref('');
const searchResults = ref([]);
const searchPostRes = ref([]);
//無限讀取用
const page = ref(1); // Current page of data
const perPage = ref(10); // Number of items per page
const maxPages = ref(50); // Maximum number of pages
const loading = ref(true); // Loading state
const newCards = ref([])
const searchUser = async () => {
  if (searchQuery.value.trim() === "") {
    searchResults.value = [];
    return;
  }

  try {
    const response = await axiosapi.get('/api/friends/searchUser', {
      params: { name: searchQuery.value },
      headers: {
        'session-id': sessionStorage.getItem('Session-Id')
      }
    });
    if (response.data.success) {
      const senderId = sessionStorage.getItem('Session-Id');

      const userInfos = await Promise.all(response.data.userInfos.map(async (user) => {
        const checkResponse = await axiosapi.post('/api/friends/checkRelationship', {
          user1Id: senderId,
          user2Id: user.userId
        }, {
          headers: {
            'session-id': sessionStorage.getItem('Session-Id')
          }
        });

        return {
          ...user,
          isFollowing: checkResponse.data.status === 2 || checkResponse.data.status === 3
        };
      }));

      searchResults.value = userInfos;
    } else {
      searchResults.value = [];
      console.error('找不到符合的用戶');
    }
  } catch (error) {
    searchResults.value = [];
    console.error('查詢失敗:', error);
  }
};

const toggleFollow = async (user) => {
  const senderId = sessionStorage.getItem('Session-Id');
  try {
    if (user.isFollowing) {
      await axiosapi.delete('/api/friends/delete', {
        data: {
          senderId: senderId,
          receiverId: user.userId
        },
        headers: {
          'session-id': sessionStorage.getItem('Session-Id')
        }
      });
      user.isFollowing = false;
    } else {
      await axiosapi.post('/api/friends/add', {
        senderId: senderId,
        receiverId: user.userId
      }, {
        headers: {
          'session-id': sessionStorage.getItem('Session-Id')
        }
      });
      user.isFollowing = true;
    }
  } catch (error) {
    console.error('操作失敗:', error);
  }
};

const posts = async (value) => {
  if (searchQuery.value.trim() === "") {
    searchPostRes.value = [];
    page.value = 1;
    loading.value = true;
    return;
  }
  console.log("searchQuery.value:", searchQuery.value);
  console.log("page.value:", page.value);
  let sentence = { "sentence": searchQuery.value, "page": page.value }
  axiosapi.post("ajax/post/searchPost", sentence).then((response) => {
    if (page.value == 1) {
      searchPostRes.value = response.data.posts;
      console.log("size:", searchPostRes.value.length)
      loading.value = false;
    } else {
      newCards.value = response.data.posts;
    }
  })
};
//無限讀取

onMounted(() => {
  // Start observing when component is mounted
  const observerTarget = document.querySelector('.observer-target');

  if (observerTarget) {
    observer.observe(observerTarget);
  }

});

// Hook: Before component is unmounted
onBeforeUnmount(() => {
  // Stop observing and clean up
  observer.disconnect();
});

const loadMoreCards = async () => {
  if (page.value <= maxPages.value && !loading.value && searchPostRes.value.length >= 9) {
    loading.value = true; // Set loading state to true
    page.value++;
    await posts(); // Fetch new cards

    searchPostRes.value = [...searchPostRes.value, ...newCards.value]; // Append new cards
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

</script>

<style scoped>
.centered-container {
  display: flex;
  justify-content: center;
  /* 讓容器鋪滿整個視窗高度 */
}

.observer-target {
  height: 10px;
  /* Adjust height as needed for observer target */
  margin: 20px 0;
  /* Add margin to ensure intersection triggers appropriately */
}
</style>
