<template>
  <div class="comment">
    <div class="header">
      <a class="avatar">
        <RouterLink class="nav-link" :to="'/views/Friend?id=' + comment.userInfoId">
          <img :src="comment.posterPhoto" alt="Avatar">
        </RouterLink>
        {{ comment.avatar }}
      </a>
      <div class="info">
        <div class="metadata">
          <RouterLink class="nav-link" :to="'/views/Friend?id=' + comment.userInfoId">
            <a class="author">{{ comment.posterName }}</a>
          </RouterLink>
          <span class="date">{{ formatTime(comment.addTime) }}</span>
          <div class="ui compact menu">
            <div v-show="show" class="ui simple dropdown item">
              <i class="ellipsis vertical icon"></i>
              <div class="menu">
                <button class="item" @click="emits('deleteComment', comment.commentId)">
                  <i class="trash icon"></i> 删除
                </button>
                <!--
                <button class="item" @click="showModal">
                  <i class="edit icon"></i> 编辑
                </button>
                -->
              </div>
            </div>
          </div>
        </div>
        <div class="text">
          {{ comment.content || 'No content available' }}
        </div>
      </div>
    </div>
    <br>
    <div class="divider"></div>
    <!-- 引入新的模態框組件 -->
    <EditModal ref="editCommentModal" />
  </div>
</template>

<script setup>
import axiosapi from '@/plugins/axios';
import { differenceInDays, differenceInHours, differenceInMinutes, differenceInSeconds } from 'date-fns';
import { defineProps, ref, onMounted } from 'vue';
import EditModal from './CommentEditModal.vue';


axiosapi.defaults.headers['session-id'] = sessionStorage.getItem("Session-Id");
const props = defineProps(["index", "comment", "show"])
const emits = defineEmits(["deleteComment"])
const postId = ref('');
const params = new URLSearchParams(window.location.search);
postId.value = params.get('id');


function formatTime(time) {
  if (!time) return '时间未知';

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

// 建立一個 ref 來引用 EditComment 元件實例
const editCommentModal = ref(null);

function showModal() {
  if (editCommentModal.value) {
    editCommentModal.value.show();
  }
}
</script>

<style scoped>
.comment {
  margin-top: 5px;
  width: 100%;
  padding: 0;
}

.header {
  display: flex;
  align-items: flex-start;
  width: 100%;
}

.avatar {
  margin-right: 30px;
  width: 30px;
  height: 30px;
  border-radius: 50%;
}

.info {
  display: flex;
  flex-direction: column;
  width: 100%;
}

.metadata {
  display: flex;
  flex-direction: row;
  margin-bottom: 2px;
  align-items: center;
}

.date {
  position: relative;
  margin-left: 250px;
  font-size: 0.8em;
  color: #888;
}

.author {
  font-weight: bold;
  margin-bottom: 2px;
}

.text {
  font-size: 0.9em;
  text-align: left;
  margin-bottom: 2px;
}

.divider {
  border-top: 1px solid #ccc;
  width: calc(100% + 20px);
  margin-left: -10px;
}

.reply-box {
  margin-top: 5px;
}

.ui.compact.menu {
  border: none;
  box-shadow: none;
  background: none;
  margin-left: auto;
}

.modal.fade .modal-dialog {
  transition: transform .3s ease-out;
}

.modal.fade.show .modal-dialog {
  transform: translateY(0);
}
</style>
