<template>
  <div class="ui card large">
    <div class="content">
      <div class="time">{{ formatTime(card.addTime) }}</div>
      <span>
        <RouterLink class="nav-link" :to="'/views/Friend?id=' + card.posterInfoId">
          <img class="ui avatar image" v-show="card.posterPhoto !== '' && card.posterPhoto !== 'undefined'"
            :src="card.posterPhoto" alt="Base64 Image">
          {{ card.posterName }}
        </RouterLink>
      </span>
      <!-- 下拉式選單 -->
      <div v-show="show" class="ui compact menu right floated">
        <div class="ui simple dropdown item ">
          <i class="ellipsis vertical icon"></i>
          <div class="menu">
            <!-- 刪除按鈕 -->
            <button class="item" @click="deletePost(card.postId)">
              <i class="trash icon"></i> Delete
            </button>
            <!-- 編輯按鈕 -->
            <button class="item" @click="editPost(card.postId)">
              <i class="edit icon"></i> Edit
            </button>
          </div>
        </div>
      </div>
    </div>
    <div class="postImg" style="display: flex; align-items: center; justify-content: center; background-color: white;">
      <img v-show="card.image !== ''" :src="card.image" 
      style="
      border-radius: 1%;
      width: 90%;  
      height: auto;
      object-fit: contain;
      max-height: 490px;
      object-fit: fill;">
    </div>

    <div class="content">
      <div>{{ card.content }}</div>
      
      <br>
      <span class="right floated" @click="emits('clickLike')">
        <i class="heart outline like icon"></i>
        {{ card.likeCount }} likes
      </span>
      <RouterLink class="nav-link" :to="'/views/post?id=' + card.postId" @click.native="handleCommentClick">
        <i class="comment icon"></i>
        {{ card.commentNum }} comments
      </RouterLink>
    </div>
    <div class="extra content">
      <div class="ui large transparent left icon input">
        <ReplyBox v-show="showCommentBar" :postId="card.postId" />
      </div>
    </div>
    <!-- Edit Modal -->
    <EditPost :edit="edit" />
  </div>
</template>
  
<script setup>
import ReplyBox from '@/components/Post/ReplyBox.vue';
import axiosapi from '@/plugins/axios';
import { Modal } from 'bootstrap/dist/js/bootstrap.bundle.min';
import { differenceInDays, differenceInHours, differenceInMinutes, differenceInSeconds } from 'date-fns'; // 導入所需的date-fns函數
import { ref } from 'vue';
import EditPost from '../Edit/EditPost.vue';
const props = defineProps(["card", "show", "showCommentBar"]);
const emits = defineEmits(["clickLike", "comment-click"]);
const edit = ref({ content: '', image: null, postId: null, permission: '1' });

axiosapi.defaults.headers['session-id'] = sessionStorage.getItem("Session-Id");

const defaultImage = 'https://t4.ftcdn.net/jpg/02/86/37/57/360_F_286375793_tMNCezCxZVJMzXVlunBoKI1JWP72cE9L.jpg';

function formatTime(time) {
  const now = new Date();
  const targetDate = new Date(time);
  const secondsDifference = differenceInSeconds(now, targetDate);
  const minutesDifference = differenceInMinutes(now, targetDate);
  const hoursDifference = differenceInHours(now, targetDate);
  const daysDifference = differenceInDays(now, targetDate);

  if (secondsDifference < 60) {
    return `${secondsDifference}秒前`;
  } else if (minutesDifference < 60) {
    return `${minutesDifference}分鐘前`;
  } else if (hoursDifference < 24) {
    return `${hoursDifference}小時前`;
  } else {
    return `${daysDifference}天前`;
  }
}

function deletePost(postId, onSuccess, onError) {
  axiosapi.delete(`/ajax/post/deletePost/${postId}`)
    .then(response => {
      console.log(`Post ${postId} deleted successfully.`);
      // 呼叫成功回呼函數，更新父元件中的貼文清單或重新取得數據
      if (onSuccess) {
        onSuccess(response);
      }
      window.location.replace('/home');
    })
    .catch(error => {
      console.error(`Error deleting post ${postId}:`, error);
      // 呼叫錯誤回呼函數，處理刪除失敗的情況
      if (onError) {
        onError(error);
      }
    });
}


const editPost = (postId) => {
  const post = props.card;
  edit.value = { content: post.content, image: post.image, postId: postId };

  const editModalElement = document.getElementById('editModal');
  if (editModalElement) {
    const editModal = Modal.getInstance(editModalElement) || new Modal(editModalElement);
    if (editModal) {
      editModal.show();
    } else {
      console.log('Failed to get the modal instance.');
    }
  } else {
    console.log('Failed to find the modal element.');
  }
};
const handleCommentClick = (comment) => {
  emits('comment-click', comment); 
};
</script>
  
<style scoped>
/* 如果需要，可以添加局部作用域的樣式 */
.nav-link {
  display: inline;
}

.content {
  width: 100%;
  height: auto;
  font-size: 1.3em;
}

.time{
  position: absolute;
  right: 80px;
  margin: 0;
  padding: 0px;
}

.ui.card.large {
  /* 确保卡片也被调整大小 */
  width: 470px;
  height: auto;
  font-size: 1.3em;
}

.ui.avatar.image {
  width: 50px;
  height: 50px;
  object-fit: cover;
}

.date-and-menu {
  position: relative;
}

.ui.compact.menu.right.floated {
  position: absolute;
  top: 0;
  right: 0;
  border: none;
  box-shadow: none;
  background: none;
  padding: 0;
  font-size: 20px;
}

.postImg{
  max-width: 100%;
  max-height: 100%;
}

</style>
  