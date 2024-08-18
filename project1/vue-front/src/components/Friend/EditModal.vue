<template>
    <div class="modal">
      <div class="modal-content">
        <span class="close" @click="close">&times;</span>
        <h2 class="textH1">編輯個人檔案</h2>
        <div class="profile-header">
          <img class="profile-image" v-lazy="user.photo || defaultAvatar" :alt="noImg">
          <div class="profile-info">
            <h2>{{ user.userFirstName }} {{ user.userLastName }}</h2>
            <p>{{ user.email }}</p>
          </div>
        </div>
        <form>
          <div class="form-group">
            <label for="firstName">姓氏</label>
            <input type="text" id="firstName" name="firstName" v-model="userForm.userFirstName" placeholder="姓氏">
          </div>
          <div class="form-group">
            <label for="lastName">名字</label>
            <input type="text" id="lastName" name="lastName" v-model="userForm.userLastName" placeholder="名字">
          </div>
          <div class="form-group">
            <label for="birth">生日</label>
            <input type="date" id="birth" name="birth" v-model="userForm.birth">
          </div>
          <div class="form-group">
            <label for="phone">聯絡資訊</label>
            <input type="text" id="phone" name="phone" v-model="userForm.phone" placeholder="phone"
              @input="validatePhoneInput" required>
            <span v-if="phoneError">請輸入8到10個數字</span>
          </div>
          <div class="form-group">
            <label for="selfContent">個人簡介</label>
            <textarea id="selfContent" name="selfContent" maxlength="120" v-model="userForm.selfContent"
              placeholder="個人簡介"></textarea>
            <p class="char-count">{{ userForm.selfContent ? userForm.selfContent.length : 0 }} / 120</p>
          </div>
          <div class="form-group">
            <label for="gender">性別</label>
            <select id="gender" name="gender" v-model="userForm.gender">
              <option value="1">男性</option>
              <option value="2">女性</option>
              <option value="3">其他</option>
            </select>
            <p class="hint">這不會顯示在你的公開個人檔案中。</p>
          </div>
          <button type="button" class="save-button" @click="saveChanges">儲存變更</button>
        </form>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue';
  import axiosapi from '@/plugins/axios';
  import Swal from 'sweetalert2';
  import { defineProps, defineEmits } from 'vue';
  
  const props = defineProps({
    user: {
      type: Object,
      required: true,
    },
  });
  
  const emits = defineEmits(['close', 'user-updated']);
  
  const userForm = ref({ ...props.user });
  const phoneError = ref(false);
  
  const close = () => {
    emits('close');
  };
  const showSettingsModal = ref(false);
  
  const toggleSettingsModal = () => {
  userForm.value = {
    birth: props.user.birth,
    gender: props.user.gender,
    phone: props.user.phone,
    selfContent: props.user.selfContent,
    userFirstName: props.user.userFirstName,
    userLastName: props.user.userLastName
  };
  showSettingsModal.value = !showSettingsModal.value;
};

  const saveChanges = async () => {
  try {
    const sessionId = sessionStorage.getItem('Session-Id');
    if (!sessionId) {
      throw new Error('Session ID not found');
    }

    const userData = {};
    if (userForm.value.userFirstName) userData.userFirstName = userForm.value.userFirstName;
    if (userForm.value.userLastName) userData.userLastName = userForm.value.userLastName;
    if (userForm.value.birth) userData.birth = userForm.value.birth;
    if (userForm.value.phone) userData.phone = userForm.value.phone;
    if (userForm.value.selfContent) userData.selfContent = userForm.value.selfContent;
    if (userForm.value.gender) userData.gender = userForm.value.gender.toString();

    const response = await axiosapi.put(
      '/api/userInfo/updateInfo',
      userData,
      {
        headers: {
          'Content-Type': 'application/json',
          'session-id': sessionId,
        },
      }
    );

    if (response.data.success) {
      console.log('用戶信息更新成功');
      Object.assign(props.user, userForm.value);
      close();
    } else {
      console.error('用戶信息更新失敗:', response.data.message);
    }
  } catch (error) {
    console.error('保存變更時發生錯誤:', error);
  }
  toggleSettingsModal();
};
  
  const validatePhoneInput = (event) => {
    const phone = event.target.value.replace(/\D/g, '');
    event.target.value = phone;
    phoneError.value = phone.length < 8 || phone.length > 10;
  };
  </script>
  
  <style scoped>
  @import '@/assets/personalPage.css';
  @import '@/assets/personalUpdate.css';
  </style>
  