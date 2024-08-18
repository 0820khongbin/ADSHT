<template>

  <body>
    <!-- 個人頁面上半部 -->
    <div v-if="personalLoading">
      <SkeletonPersonal />
    </div>
    <div v-else>
      <div v-if="user" class="plx-card gold">
        <div class="pxc-bg"
          style="background-image:url('https://png.pngtree.com/background/20230410/original/pngtree-powder-smoke-colorful-background-picture-image_2379658.jpg')">
        </div>
        <font-awesome-icon icon="fa-solid fa-chess-king" class="king-icon" @click="showSubsEdit()"
          v-if="user.userId == senderId" />
        <font-awesome-icon icon="fa-solid fa-gear" class="gear-icon" v-if="user.userId == senderId"
          @click="toggleSettingsModal" />

        <div class="pxc-avatar">
          <div class="image">
            <div class="circle-1"></div>
            <div class="circle-2"></div>
            <!-- 個人圖片 -->
            <div class="avatar-upload">
              <div class="avatar-edit" v-if="user.userId == senderId">
                <input type="file" id="imageUpload" accept=".png, .jpg, .jpeg" @change="previewFile" />
                <label for="imageUpload">
                  <font-awesome-icon icon="fa-solid fa-pencil"></font-awesome-icon>
                </label>
              </div>
              <img :src="user.photo || defaultAvatar" width="70" height="70" :alt="noImg">
            </div>
          </div>
        </div>
        <div class="pxc-stopper"></div>
        <div class="pxc-subcard">
          <div class="content">
            <div class="pxc-title">{{ user.userFirstName }} {{ user.userLastName }}</div>
            <div class="pxc-sub"></div>
            <div class="pxc-feats">
              <!-- 測試區 -->
              <span>{{ user.selfContent }}</span>
            </div>
            <div v-if="mySubs.length !== 0" class="subsImg">
              <img class="subsImg1" v-for="mySub in mySubs.slice(0, 3)" v-lazy="mySub.Image" width="50" height="50"
                :alt="noImg">
            </div>
            <div class="pxc-tags">
              <button v-if="user.userId !== senderId" @click="handleFollowClick">
                {{ user.isFollowing ? '已追蹤' : '追蹤' }}
              </button>
              <button v-if="user.userId !== senderId" @click="goToOtherSubs(user.userId, user.userLastName)">訂閱</button>
            </div>
            <div class="bottom-row">
              <div class="pxc-info">
                <div class="flags"></div>
              </div>
            </div>
          </div>
          <div class="stats">
            <div class="box no-click">
              <span class="value">{{ PostCount }}</span>
              <span class="parameter">貼文</span>
            </div>
            <div class="box" @click="openFollowersModal">
              <span class="value">{{ followersCount }}</span>
              <span class="parameter">粉絲</span>
            </div>
            <div class="box" @click="openFollowingModal">
              <span class="value">{{ followingCount }}</span>
              <span class="parameter">關注</span>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- 個人頁面上半部 -->


    <!-- Followers Modal -->
    <UserListModal v-if="showFollowersModal" @close="closeFollowersModal" :type="'followers'" :userId="user.userId"
      @user-clicked="handleUserClicked" @toggleFollow="handleToggleFollowFromModal"></UserListModal>

    <!-- Following Modal -->
    <UserListModal v-if="showFollowingModal" @close="closeFollowingModal" :type="'following'" :userId="user.userId"
      @user-clicked="handleUserClicked" @toggleFollow="handleToggleFollowFromModal"></UserListModal>

    <!-- 分隔區1 -->
    <div class="lineheader">
      <div class="linecontainer">
        <div class="line">
          <a href="#" @click.prevent="showPosts">貼文</a>
          <a href="#" @click.prevent="showShops">商店</a>
          <a href="#" @click.prevent="showSubsMebPhoto">會員專屬</a>
        </div>
        <!-- 右側按鈕 -->
        <button v-if="currentView === 'posts'" class="order-toggle" @click="togglePostOrder">
          {{ isAsc ? '由新至舊' : '由舊至新' }}
        </button>
      </div>
    </div>

    <!-- 分隔區2 -->
    <!-- 貼文 start -->
    <PostSection 
      v-if="currentView === 'posts'"
      :posts="posts" 
      :dataLoaded="dataLoaded" 
      :noPosts="noPosts" 
      :defaultImage="defaultImage"
      @open-image-modal="openImageModal"
    />
    <!-- 貼文 end -->

    <!--訂閱管理-->
    <div v-if="currentView === 'subsEdit'">
      <div>
        <!--image row start-->
        <div class="row">
          <SubListCompo1></SubListCompo1>
        </div>
      </div>
    </div>
    <!--訂閱管理 end-->

    <!--會員圖片顯示-->
    <div v-if="currentView === 'subsMebPhotos'">
      <div>
        <!--image row start-->
        <div class="row">
          <!-- <div v-for=" subsPhoto in subsPhotos"> -->
          <FindMySubsPhotoCompo :productA="subsPhotos" :user="user">
          </FindMySubsPhotoCompo>
          <!-- </div> -->
        </div>
      </div>
    </div>
    <!--會員圖片顯示 end-->


    <!-- shop區域 start -->
    <ShopSection 
      v-if="currentView === 'shops'" 
      :shops="shops" 
      :shopDataLoaded="shopDataLoaded" 
      :noShops="noShops" 
      :defaultImage="defaultImage"
    />
    <!-- shop區域 end -->

    <!-- 修改頁面彈出視窗 -->
    <EditModal 
      v-if="showSettingsModal"
      :user="user"
      @close="toggleSettingsModal"
      @user-updated="updateUserInfo"
    />
    <!-- 修改頁面彈出視窗 -->

    <!-- 個人貼文彈出視窗 -->
    <div v-if="showImageModal" class="image-modal">
      <div class="image-modal-content">
        <span class="image-modal-close" @click="closeImageModal">&times;</span>
        <div class="personalPost-container">
          <div class="image-section">
            <img :src="currentPost?.image || defaultImage" alt="Profile Picture">
          </div>
          <div class="info-section">
            <div class="header">
              <img :src="user.photo || defaultAvatar" alt="Profile Picture">
              <div class="username">{{ user.userFirstName }} {{ user.userLastName }}</div>
            </div>
            <div class="comments">
              <!-- post內容 -->
              <div class="personalLink">
                <img :src="user.photo || defaultAvatar" alt="Profile Picture">
                <div class="description">
                  <div class="user-info">
                    <div class="user-title">{{ user.userFirstName }} {{ user.userLastName }}</div>
                    <p class="user-sub" :title="formatDate(currentPost?.addTime)">{{
                      formatRelativeTime(currentPost?.addTime) }}</p>
                  </div>
                  <div class="greeting">{{ currentPost?.content }}</div>
                </div>
              </div>

              <!-- postCommet內容 -->
              <div v-for="comment in postComments" :key="comment.id" class="personalLink">
                <img :src="comment.userPhoto || defaultAvatar" alt="Profile Picture"
                  @click="handleCommentUserClicked(comment.userInfoId)">
                <div class="description">
                  <div class="user-info">
                    <div class="user-title">{{ comment.userFirstName }} {{ comment.userLastName }}</div>
                    <p class="user-sub" :title="formatDate(comment.addTime)">{{ comment.formattedTime }}</p>
                  </div>
                  <div class="greeting">{{ comment.content }}</div>
                </div>
              </div>
              <!-- postCommet內容結尾 -->
            </div>

            <div class="add-comment">
              <input type="text" v-model="newCommentContent" placeholder="留言...">
              <button @click="addComment">發佈</button>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- 個人貼文彈出視窗 -->

    <!-- 取消追蹤彈出視窗 -->
    <div v-if="showUnfollowModal" class="follow-modal" @click.self="closeModal">
      <div class="follow-modal-content">
        <span class="close" @click="toggleUnfollowModal">&times;</span>
        <img :src="user.photo || defaultAvatar" alt="User Image">
        <div class="username">{{ user.userFirstName }} {{ user.userLastName }}</div>
        <div class="follow-linecontainer"></div>

        <div class="follow-modal-operator">
          <p @click="confirmUnfollow">
            取消追蹤
            <font-awesome-icon icon="fa-solid fa-chevron-right" />
          </p>
        </div>
      </div>
    </div>
    <!-- 取消追蹤彈出視窗 -->
  </body>
</template>

<script setup>
import { ref, computed, watch, onMounted } from 'vue';
import { defineProps, defineEmits } from 'vue';
import axiosapi from '@/plugins/axios';
import UserListModal from '@/components/Friend/UserListModal.vue';
import { useRouter, useRoute } from 'vue-router';
import Swal from 'sweetalert2';
import SubListCompo1 from '../Subscriptions/SubListCompo1.vue';
import "vue3-loading-skeleton/dist/style.css";
import SkeletonPersonal from './SkeletonPersonal.vue';
import FindMySubsPhotoCompo from '../Subscriptions/FindMySubsPhotoCompo.vue';

import PostSection from './PostSection.vue';
import ShopSection from './ShopSection.vue';
import EditModal from './EditModal.vue';

const noImg = 'User\'s profile image';

const props = defineProps({
  user: {
    type: Object,
    default: () => ({ userFirstName: '', userLastName: '', photo: '', userId: '', isFollowing: false, state: 0, birth: '', phone: '', selfContent: '', gender: '' })
  },
  senderId: String,
  followersCount: Number,
  followingCount: Number,
  PostCount: Number,
});

const emits = defineEmits(['toggleFollow', 'user-clicked']);
const router = useRouter();
const route = useRoute();

const showFollowersModal = ref(false);
const showFollowingModal = ref(false);
const showUnfollowModal = ref(false);
const personalLoading = ref(true);
const dataLoaded = ref(false);
const shopDataLoaded = ref(false);
const mySubs = ref([]);
const subsPhotos = ref([]);


const noPosts = computed(() => !posts.value || posts.value.length === 0);
const noShops = computed(() => {
  const result = !shops.value || shops.value.every(shop => !shop.products || shop.products.length === 0);
  return result;
});

const posts = ref([]);
const shops = ref([]);
const newCommentContent = ref("");
const currentView = ref('posts');  // State to manage the current view
const defaultAvatar = 'https://img.freepik.com/premium-vector/default-avatar-profile-icon-social-media-user-image-gray-avatar-icon-blank-profile-silhouette-vector-illustration_561158-3396.jpg?w=360';
const defaultImage = 'https://t4.ftcdn.net/jpg/02/86/37/57/360_F_286375793_tMNCezCxZVJMzXVlunBoKI1JWP72cE9L.jpg';


// 彈窗邏輯
const openFollowersModal = () => {
  showFollowersModal.value = true;
};

const closeFollowersModal = () => {
  showFollowersModal.value = false;
};

const openFollowingModal = () => {
  showFollowingModal.value = true;
};

const closeFollowingModal = () => {
  showFollowingModal.value = false;
};


const handleFollowClick = () => {
  if (props.user.isFollowing) {
    toggleUnfollowModal();
  } else {
    handleToggleFollow();
  }
};


const handleToggleFollow = () => {
  if (!props.senderId) {
    router.push('/views/login');
    return;
  }
  emits('toggleFollow', { user: props.user, source: 'personal' });
};

const handleToggleFollowFromModal = (user) => {
  emits('toggleFollow', { user, source: 'userListModal' });
};

const handleUserClicked = (userId) => {
  closeFollowersModal();
  closeFollowingModal();
  router.push({ name: 'user-profile', query: { id: userId } });
};

const handleCommentUserClicked = (userId) => {
  closeImageModal();
  router.push({ name: 'user-profile', query: { id: userId } });
};

function previewFile(event) {
  const file = event.target.files[0];
  const reader = new FileReader();

  reader.onload = (e) => {
    props.user.photo = e.target.result;
    uploadImage(e.target.result);
  };

  if (file) {
    reader.readAsDataURL(file);
  }
};

async function uploadImage(base64Image) {
  try {
    const sessionId = sessionStorage.getItem('Session-Id');
    if (!sessionId) {
      throw new Error('Session ID not found');
    }
    const response = await axiosapi.put(
      '/api/userInfo/updatePhoto',
      { image: base64Image },
      {
        headers: {
          'Content-Type': 'application/json',
          'session-id': sessionId,
        },
      }
    );

    if (response.data.success) {
      console.log('圖片更新成功');
      Swal.fire({
        title: "上傳成功!",
        text: "您的圖片已成功上傳。",
        icon: "success"
      });
    } else {
      console.error('圖片更新失敗:', response.data.message);
      Swal.fire({
        title: "上傳失敗",
        text: response.data.message,
        icon: "error"
      });
    }
  } catch (error) {
    console.error('上傳圖片時發生錯誤:', error);
    Swal.fire({
      title: "上傳失敗",
      text: '上傳圖片時發生錯誤',
      icon: "error"
    });
  }
};

const showSettingsModal = ref(false);

const toggleSettingsModal = () => {
  showSettingsModal.value = !showSettingsModal.value;
};

const toggleUnfollowModal = () => {
  showUnfollowModal.value = !showUnfollowModal.value;
};

const confirmUnfollow = () => {
  toggleUnfollowModal();
  handleToggleFollow();
};

const isAsc = ref(false);
// 切換排序順序
const togglePostOrder = () => {
  isAsc.value = !isAsc.value; 
  fetchUserPosts(); // 重新獲取貼文
};
const fetchUserPosts = async () => {
  dataLoaded.value = false; // 加載開始時設置為false
  posts.value = [];
  try {
    const sessionId = props.user.userId;
    if (!sessionId) {
      throw new Error('User ID not found');
    }
    const response = await axiosapi.post(
      '/api/friends/userPostsFind',
      {
        asc: isAsc.value,
        userId: props.user.userId
      },
      {
        headers: {
          'Content-Type': 'application/json',
          'session-id': sessionId,
        }
      }
    );

    if (response.data.error) {
      console.error('獲取貼文失敗:', response.data.error);
    } else {
      posts.value = response.data.posts.map(post => {
        post.addTime = post.addTime;
        post.formattedAddTime = formatRelativeTime(post.addTime);
        return post;
      });
    }
    dataLoaded.value = true; // 加載完成後設置為true

  } catch (error) {
    console.error('獲取貼文時發生錯誤:', error);
    dataLoaded.value = true; // 即使失敗也設置為true，以避免不必要的等待
  }
};


const formatDate = (dateString) => {
  const date = new Date(dateString);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  return `${year}年${month}月${day}日`;
};

const formatRelativeTime = (dateString) => {
  const date = new Date(dateString);
  const now = new Date();
  const diff = Math.abs(now - date);
  const diffDays = Math.floor(diff / (1000 * 60 * 60 * 24));

  if (diffDays < 1) {
    const diffHours = Math.floor(diff / (1000 * 60 * 60));
    if (diffHours < 1) {
      const diffMinutes = Math.floor(diff / (1000 * 60));
      return `${diffMinutes} 分鐘前`;
    }
    return `${diffHours} 小時前`;
  } else if (diffDays < 7) {
    return `${diffDays} 天前`;
  } else {
    const diffWeeks = Math.floor(diffDays / 7);
    return `${diffWeeks} 週前`;
  }
};

// 獲取好友狀態
const fetchFriendState = async () => {
  try {
    const sessionId = sessionStorage.getItem('Session-Id');
    if (!sessionId) {
      throw new Error('Session ID not found');
    }

    const response = await axiosapi.post('/api/friends/state', {
      sendId: sessionId,
      receiveId: props.user.userId
    }, {
      headers: {
        'Content-Type': 'application/json',
        'session-id': sessionId,
      },
    });

    if (response.data.success) {
      props.user.state = response.data.state;
    } else {
      console.error('獲取好友狀態失敗:', response.data.message);
    }
  } catch (error) {
    console.error('獲取好友狀態時發生錯誤:', error);
  } finally {
    personalLoading.value = false; // 個人資料加載完成後設置為 false
  }
};


watch(() => props.user, async (newUser) => {
  if (newUser && newUser.userId) {
    await fetchFriendState();
    if (currentView.value === 'posts') {
      await fetchUserPosts();
    }
    if (currentView.value === 'shops') {
      await fetchUserShops();
    }
    await callFindMySubs(newUser.userId);
  }
}, { immediate: true });


const showImageModal = ref(false);
const currentPost = ref(null);
const postComments = ref([]);


const openImageModal = async (post) => {
  currentPost.value = post;

  try {
    const sessionId = sessionStorage.getItem('Session-Id');
    if (!sessionId) {
      throw new Error('Session ID not found');
    }
    const postCommentsResponse = await axiosapi.post(
      `/ajax/comments/findPostComment/${post.postId}`,
      {},
      {
        headers: {
          'Content-Type': 'application/json',
          'session-id': sessionId,
        }
      }
    );

    if (postCommentsResponse.data.error) {
      console.error('獲取留言失敗:', postCommentsResponse.data.error);
    } else {
      const comments = postCommentsResponse.data.comments;
      const userInfoPromises = comments.map(comment => fetchUserInfo(comment.user));

      const userInfoResponses = await Promise.all(userInfoPromises);

      userInfoResponses.forEach((response, index) => {
        if (response.success) {
          comments[index].userPhoto = response.userInfo.photo;
          comments[index].userFirstName = response.userInfo.userFirstName;
          comments[index].userLastName = response.userInfo.userLastName;
        } else {
          comments[index].userPhoto = defaultAvatar;
          comments[index].userFirstName = "未知";
          comments[index].userLastName = "用戶";
        }
        comments[index].formattedTime = formatRelativeTime(comments[index].addTime);
      });

      postComments.value = comments;
    }
  } catch (error) {
    console.error('獲取留言時發生錯誤:', error);
  }

  currentPost.value.formattedAddTime = formatRelativeTime(currentPost.value.addTime);
  showImageModal.value = true;
};

const closeImageModal = () => {
  showImageModal.value = false;
  currentPost.value = null;
  postComments.value = [];
};

const fetchUserInfo = async (userId) => {
  try {
    const sessionId = sessionStorage.getItem('Session-Id');
    if (!sessionId) {
      throw new Error('Session ID not found');
    }

    const response = await axiosapi.post(
      '/api/userInfo/findUserInfoByUserId',
      { userId: userId },
      {
        headers: {
          'Content-Type': 'application/json',
          'session-id': sessionId,
        }
      }
    );

    return response.data;
  } catch (error) {
    console.error('獲取用戶信息時發生錯誤:', error);
    return { success: false };
  }
};

const addComment = async () => {
  if (!newCommentContent.value.trim()) {
    alert("留言內容不能為空");
    return;
  }

  try {
    const sessionId = sessionStorage.getItem('Session-Id');
    if (!sessionId) {
      throw new Error('Session ID not found');
    }

    const response = await axiosapi.post(
      `/ajax/comments/newComment/${currentPost.value.postId}`,
      { content: newCommentContent.value },
      {
        headers: {
          'Content-Type': 'application/json',
          'session-id': sessionId,
        }
      }
    );

    if (response.data.error) {
      console.error('新增留言失敗:', response.data.error);
      alert(response.data.error);
    } else {
      console.log('新增留言成功:', response.data);
      newCommentContent.value = "";
      openImageModal(currentPost.value);
    }
  } catch (error) {
    console.error('新增留言時發生錯誤:', error);
    alert('新增留言時發生錯誤');
  }
};

// 更新好友狀態
const updateFriendState = async (state) => {
  try {
    const sessionId = sessionStorage.getItem('Session-Id');
    if (!sessionId) {
      throw new Error('Session ID not found');
    }

    const data = {
      id: {
        sendId: props.senderId,
        receiveId: props.user.userId,
      },
      state: state
    };

    const response = await axiosapi.put('/api/friends/update', data, {
      headers: {
        'Content-Type': 'application/json',
        'session-id': sessionId,
      },
    });

    if (response.data.success) {
      console.log('更新成功');
      props.user.state = state; // 更新本地狀態
    } else {
      console.error('更新失敗:', response.data.message);
    }
  } catch (error) {
    console.error('更新好友狀態時發生錯誤:', error);
  }
};

const showPosts = () => {
  currentView.value = 'posts';
  fetchUserPosts();
};

const showShops = () => {
  currentView.value = 'shops';
  fetchUserShops();
};

const showSubsEdit = () => {
  currentView.value = 'subsEdit';
};

const showSubsMebPhoto = () => {
  currentView.value = 'subsMebPhotos';
  callFindSubsPhotos();
};

// 當組件掛載時獲取好友狀態
onMounted(async () => {
  if (props.user && props.user.userId) {
    await fetchUserPosts();
    await fetchFriendState();

  }
});

const closeModal = () => {
  showUnfollowModal.value = false;
};

const fetchUserShops = async () => {
  shopDataLoaded.value = false; // 開始加載時設置為 false
  shops.value = [];
  const owner = props.user.userId;
  if (!owner) {
    throw new Error('User ID not found');
  }
  try {
    const response = await axiosapi.post('/api/userInfo/getUserShopsAndProducts', { userId: owner });
    if (response.data.success) {
      shops.value = response.data.shops;
    } else {
      console.error('Failed to fetch shops and products:', response.data.message);
    }
    shopDataLoaded.value = true; // 加載完成後設置為 true
  } catch (error) {
    console.error('Error fetching shops and products:', error);
    shopDataLoaded.value = true; // 即使失敗也設置為 true，以避免不必要的等待
  }
};

function goToOtherSubs(userId, username) {
  console.log("userId：", userId);
  console.log("userName：", username);
  router.push({ name: 'sublist8-page-link', query: { id: userId, name: username } });
}

const callFindMySubs = async (sessionId) => {
  axiosapi.defaults.headers['session-id'] = sessionId;
  await axiosapi.post("/ajax/pages/servicesInfo/findMySubsById").then(function (response) {
    console.log("response:", response);
    mySubs.value = response.data.ServList;
  }).catch(function (error) {
    console.log("error", error);
    Swal.fire({
      text: "查詢失敗：" + error.message,
      icon: "error"
    });
  });
}

function callFindSubsPhotos() {
  axiosapi.defaults.headers['session-id'] = sessionStorage.getItem('Session-Id');
  axiosapi.post("/ajax/pages/servicesInfo/findMySubsPhotoById").then(function (response) {
    console.log("response:", response);
    subsPhotos.value = response.data.ServList;
  }).catch(function (error) {
    console.log("error", error);
    Swal.fire({
      text: "查詢失敗：" + error.message,
      icon: "error"
    });
  });
}
//SEAN END//

</script>

<style scoped>
@import '@/assets/personalPage.css';
@import '@/assets/personalUpdate.css';
@import '@/assets/personalPost.css';
</style>