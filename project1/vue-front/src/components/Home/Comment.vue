<template>
    <div class="ui comments">
      <h3 class="ui dividing header">Comments</h3>
  
      <!-- 單一評論的範例 -->
       <h3>單一評論的範例</h3>
      <div class="comment" v-for="(comment, index) in comments" :key="index">
        <a class="avatar">
          <img :src="comment.avatar">
        </a>
        <div class="content">
          <a class="author">{{ comment.author }}</a>
          <div class="metadata">
            <span class="date">{{ comment.date }}</span>
          </div>
          <div class="text">
            {{ comment.text }}
          </div>
          <div class="actions">
            <a class="reply">Reply</a>
          </div>
        </div>
  
        <!-- 如果有回复，則遞歸顯示回复 -->
        <div v-if="comment.replies && comment.replies.length > 0" class="comments">
          <div class="comment" v-for="(reply, idx) in comment.replies" :key="idx">
            <a class="avatar">
              <img :src="reply.avatar">
            </a>
            <div class="content">
              <a class="author">{{ reply.author }}</a>
              <div class="metadata">
                <span class="date">{{ reply.date }}</span>
              </div>
              <div class="text">
                {{ reply.text }}
              </div>
              <div class="actions">
                <a class="reply">Reply</a>
              </div>
            </div>
          </div>
        </div>
      </div>
  
      <!-- 回覆表單 -->
      <form class="ui reply form">
        <div class="field">
          <textarea placeholder="Add a comment..."></textarea>
        </div>
        <div class="ui blue labeled submit icon button">
          <i class="icon edit"></i> Add Reply
        </div>
      </form>
    </div>
  </template>
  
  <script>
  export default {
    props: {
      comments: {
        type: Array,
        required: true
      }
    }
  };
  </script>
  
  <style scoped>
  .comment {
    margin-bottom: 20px;
  }
  .comments {
    margin-left: 50px; 
  }
  </style>
  