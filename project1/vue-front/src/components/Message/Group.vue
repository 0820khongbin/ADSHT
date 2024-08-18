<template>
  <div class="container my-5">
    <div class="row">
      <div class="col-md-3">
        <h1>建立群組</h1>
        <div class="card p-2 mb-3">
          <div class="form-group mb-2">
            <input v-model="newGroupName" class="form-control form-control-sm" placeholder="群組名稱" />
            <!-- 使用 form-control-sm -->
          </div>
          <div class="form-group mb-2">
            <select v-model="selectedStatus" class="form-select form-select-sm"> <!-- 使用 form-select-sm -->
              <option value="PUBLIC">公開</option>
              <option value="PRIVATE">私密</option>
            </select>
          </div>
          <div class="form-group mb-2">
            <input type="file" @change="handleFileUpload" class="form-control form-control-sm" />
            <!-- 使用 form-control-sm -->
          </div>
          <button @click="createGroup" class="btn btn-primary btn-sm w-100">建立</button> <!-- 使用 btn-sm -->
          <p v-if="createGroupMessage" class="mt-2 text-success">{{ createGroupMessage }}</p>
        </div>

        <h1>搜尋群組</h1>
        <div class="card p-2 mb-3">
          <div class="form-group mb-2">
            <input v-model="searchQuery" class="form-control form-control-sm" placeholder="輸入群組名稱" />
            <!-- 使用 form-control-sm -->
          </div>
          <button @click="searchGroup" class="btn btn-primary btn-sm w-100">搜尋</button> <!-- 使用 btn-sm -->
        </div>
      </div>

      <div class="col-md-9">
        <div v-if="searchResults.length" class="row">
          <div class="col-md-4 mb-4" v-for="(group, index) in searchResults" :key="index">
            <div class="card " style="width: 18rem;">
              <img v-if="group.image" :src="'data:image/png;base64,' + group.image" alt="Group Image"
                class="card-img-top" />
              <div v-else class="card-img-top bg-light text-center py-4">無圖片</div>
              <div class="card-body">
                <h5 class="card-title">{{ group.name }}</h5>
                <p class="card-text">{{ group.status }}</p>
                <button @click="joinGroup(group.id)" class="btn btn-primary btn-sm">加入</button> <!-- 使用 btn-sm -->
              </div>
              <div v-if="isMemberVisible" class="joinmember-card">
                <p>成員名稱和圖片</p>
                <input v-model="newMemberName" placeholder="成員名稱" class="form-control form-control-sm mb-2" />
                <!-- 使用 form-control-sm -->
                <input type="file" @change="handleMemberFileUpload" class="form-control form-control-sm mb-2" />
                <!-- 使用 form-control-sm -->
                <button @click="addMemberNameAndImage(group.id)" class="btn btn-primary btn-sm mt-2">建立</button>
                <!-- 使用 btn-sm -->
              </div>
            </div>
          </div>
        </div>

        <div class="my-groups">
          <h1>我的群組</h1>
          <div class="accordion" id="groupAccordion">
            <div class="accordion-item">
              <h2 class="accordion-header" id="publicGroupsHeader">
                <button class="accordion-button" type="button" @click="toggleVisibility('public')">
                  公開 ({{ publicGroups.length }})
                </button>
              </h2>
              <div v-if="isPublicVisible" class="accordion-collapse collapse show">
                <div class="accordion-body">
                  <ul class="list-group">
                    <li v-for="(group, index) in publicGroups" :key="index"
                      class="list-group-item d-flex justify-content-between align-items-center">
                      <div class="d-flex align-items-center">
                        <img v-if="group.image" :src="'data:image/png;base64,' + group.image" alt="Group Image"
                          class="me-3" style="width: 40px; height: 40px;" />
                        <div v-else class="bg-light me-3"
                          style="width: 40px; height: 40px; text-align: center; line-height: 40px;">無圖片</div>
                        <span>{{ group.groupName }}</span>
                      </div>
                      <div>
                        <button @click="connect(group.groupId)" class="btn btn-outline-primary btn-sm">進入</button>
                        <button @click="leaveGroup(group.groupId)" class="btn btn-outline-danger btn-sm">離開</button>
                        <button v-if="group.isAdmin" @click="manageGroup(group.groupId)"
                        class="btn btn-outline-secondary btn-sm">管理</button>



                      </div>
                    </li>
                  </ul>
                </div>
              </div>
            </div>
            <div class="accordion-item">
              <h2 class="accordion-header" id="privateGroupsHeader">
                <button class="accordion-button" type="button" @click="toggleVisibility('private')">
                  私密 ({{ privateGroups.length }})
                </button>
              </h2>
              <div v-if="isPrivateVisible" class="accordion-collapse collapse show">
                <div class="accordion-body">
                  <ul class="list-group">
                    <li v-for="(group, index) in privateGroups" :key="index"
                      class="list-group-item d-flex justify-content-between align-items-center">
                      <div class="d-flex align-items-center">
                        <img v-if="group.image" :src="'data:image/png;base64,' + group.image" alt="Group Image"
                          class="me-3" style="width: 40px; height: 40px;" />
                        <div v-else class="bg-light me-3"
                          style="width: 40px; height: 40px; text-align: center; line-height: 40px;">無圖片</div>
                        <span>{{ group.groupName }}</span>
                      </div>
                      <div>
                        <button @click="connect(group.groupId)" class="btn btn-outline-primary btn-sm">進入</button>
                        <button @click="leaveGroup(group.groupId)" class="btn btn-outline-danger btn-sm">離開</button>
                        
                          <button v-if="group.isAdmin" @click="manageGroup(group.groupId)"
                            class="btn btn-outline-secondary btn-sm">管理</button>
                          <button v-if="group.isAdmin" @click="readyallow(group.groupId)"
                            class="btn btn-outline-secondary btn-sm">審核成員</button>

                      </div>
                    </li>
                  </ul>
                </div>
              </div>
            </div>
            <div class="accordion-item">
              <h2 class="accordion-header" id="friendsGroupsHeader">
                <button class="accordion-button" type="button" @click="toggleVisibility('friends')">
                  朋友 ({{ friendsGroups.length }})
                </button>
              </h2>
              <div v-if="isFriendsVisible" class="accordion-collapse collapse show">
                <div class="accordion-body">
                  <ul class="list-group">
                    <li v-for="(group, index) in friendsGroups" :key="index"
                      class="list-group-item d-flex justify-content-between align-items-center">
                      <div class="d-flex align-items-center">
                        <img v-if="group.image" :src="'data:image/png;base64,' + group.image" alt="Group Image"
                          class="me-3" style="width: 40px; height: 40px;" />
                        <div v-else class="bg-light me-3"
                          style="width: 40px; height: 40px; text-align: center; line-height: 40px;">無圖片</div>
                        <span>{{ group.groupName }}</span>
                      </div>
                      <div>
                        <button @click="connect(group.groupId)" class="btn btn-outline-primary btn-sm">進入</button>
                      </div>
                    </li>
                  </ul>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>



    <div v-if="isManageVisible" id="sidebar" class="manage-group">
      <div class="manage-header">
        <h4>管理群組</h4>
        <button class="close-button" @click="isManageVisible = false">X</button>
      </div>
      <ul class="member-list">
        <li v-for="(member, index) in manageGroups" :key="index" class="member-card">
          <div class="member-info">
            <img :src="'data:image/png;base64,' + member.image" alt="Member Image" class="member-image" />
            <div class="member-details">
              <div>{{ member.name }}</div>
            </div>
          </div>
          <div class="member-actions">
            <button v-if="member.isCheck === true" @click="approveGroup(member)">審核</button>
            <button v-if="member.isCheck === false" @click="removeMemeber(member)">移除成員</button>
          </div>
        </li>
      </ul>
    </div>


    <div v-if="isChatRoomVisible" class="chat-room-popup">
      <div class="chat-room-header">
        <h3>聊天室</h3>
        <button class="close-button" @click="toggleChatRoom">X</button>
      </div>
      <div id="chat" class="chat-box">
        <div v-for="msg in chat" :key="msg.id" :class="['chat-message', { 'self': msg.sender_id === sessionId }]">
          <div class="message-container">
            <div class="member-info">
              <img v-if="msg.memberImage && msg.sender_id !== sessionId" :src="msg.memberImage" alt="Member Image"
                class="member-image" />
              <div v-if="msg.memberName && msg.sender_id !== sessionId" class="member-name">{{ msg.memberName }}</div>
            </div>
            <div class="message-content">
              <div v-if="msg.content" class="text-content">{{ msg.content }}</div>
              <img v-if="msg.image" :src="msg.image" alt="Image" class="chat-image" />
            </div>
          </div>
        </div>
      </div>
      <div class="chat-inputs">
        <input v-model="messageInput" type="text" placeholder="輸入消息..." />
        <input ref="imageInput" type="file" />
        <button @click="sendMessage">發送</button>
      </div>
    </div>
  </div>
</template>

    
<script setup>
import { ref, onMounted } from 'vue';
import axiosapi from '@/plugins/axios';
import Swal from 'sweetalert2';
import { nextTick } from 'vue';

// 定義組件的props
const sessionId = ref(sessionStorage.getItem("Session-Id"));
const createGroupMessage = ref("");
const newGroupName = ref("");
const selectedStatus = ref("PRIVATE");
const searchQuery = ref("");
const searchResults = ref([]);
// 成員名稱和圖片
const isMemberVisible = ref(false);
const newMemberName = ref("");
const isPublicVisible = ref(false);
const isPrivateVisible = ref(false);
const isFriendsVisible = ref(false);
const imageBase64 = ref("");

const isChatRoomVisible = ref(false);
const messageInput = ref('');
const chat = ref([]);
const imageInput = ref(null);
const ws = ref(null);

const publicGroups = ref([]);
const privateGroups = ref([]);
const friendsGroups = ref([]);
const isManageVisible = ref(false);
const manageGroups = ref([]);

const toggleVisibility = (type) => {
  if (type === 'public') {
    isPublicVisible.value = !isPublicVisible.value;
  } else if (type === 'private') {
    isPrivateVisible.value = !isPrivateVisible.value;
  } else if (type === 'friends') {
    isFriendsVisible.value = !isFriendsVisible.value;
  }
};

function handleFileUpload(event) {
  const file = event.target.files[0];
  if (file.size > 2 * 1024 * 1024) {
    Swal.fire({
      icon: "error",
      text: "圖片大小不能超過2MB",
    });
    return;
  }

  const reader = new FileReader();
  reader.onload = (e) => {
    imageBase64.value = e.target.result.split(',')[1];
  };
  reader.readAsDataURL(file);
}

function handleMemberFileUpload(event) {
  const file = event.target.files[0];
  if (file.size > 2 * 1024 * 1024) {
    Swal.fire({
      icon: "error",
      text: "圖片大小不能超過2MB",
    });
    return;
  }
  const reader = new FileReader();
  reader.onload = (e) => {
    imageBase64.value = e.target.result.split(',')[1];
  };
  reader.readAsDataURL(file);
};

function createGroup() {
  if (!imageBase64.value) {
    Swal.fire({
      icon: "error",
      text: "請選擇一張圖片",
    });
    return;
  }

  axiosapi.defaults.headers['session-id'] = sessionStorage.getItem("Session-Id");
  Swal.fire({
    text: "執行中......",
    allowOutsideClick: false,
    showConfirmButton: false,
  });

  axiosapi.post(`/ajax/pages/Group/add`, { name: newGroupName.value, status: selectedStatus.value, image: imageBase64.value })
    .then(response => {
      if (response.data.success) {
        Swal.fire({
          icon: "success",
          text: response.data.message,
        });
        createGroupMessage.value = response.data.message;
        newGroupName.value = "";
        selectedStatus.value = "PUBLIC";
        imageBase64.value = "";
        loadMyGroups();
      } else {
        Swal.fire({
          icon: "warning",
          text: response.data.message,
        });
      }
    })
    .catch(error => {
      Swal.fire({
        icon: "error",
        text: "新增錯誤：" + error.message,
      });
    });
}

// 找是否為管理者
function checkIfAdmin(groupId) {
  return new Promise((resolve, reject) => {
    axiosapi.defaults.headers['session-id'] = sessionStorage.getItem("Session-Id");
    axiosapi.get(`/ajax/pages/Group/isAdmin/${groupId}`)
      .then(response => {
        if (response.data.success) {
          resolve(response.data.isAdmin); // 假設 API 返回的是 { success: true, isAdmin: true/false }
        } else {
          Swal.fire({
            icon: "info",
            text: response.data.message,
          });
          resolve(false); // 默認情況下非管理者
        }
      })
      .catch(error => {
        Swal.fire({
          icon: "error",
          text: "錯誤：" + error.message,
        });
        reject(error);
      });
  });
}

function addMemberNameAndImage(groupId) {
  axiosapi.defaults.headers['session-id'] = sessionStorage.getItem("Session-Id");
  Swal.fire({
    text: "執行中......",
    allowOutsideClick: false,
    showConfirmButton: false,
  });

  axiosapi.post(`/ajax/pages/Group/memberNameAndImage`, { id: groupId, name: newMemberName.value, image: imageBase64.value })
    .then(response => {
      if (response.data.success) {
        Swal.fire({
          icon: "success",
          text: response.data.message,
        });
      } else {
        Swal.fire({
          icon: "warning",
          text: response.data.message,
        });
      }
    })
    .catch(error => {
      Swal.fire({
        icon: "error",
        text: "新增錯誤：" + error.message,
      });
    });
}

function searchGroup() {
  Swal.fire({
    text: "執行中......",
    allowOutsideClick: false,
    showConfirmButton: false,
  });

  axiosapi.post(`/ajax/pages/Group/search`, { name: searchQuery.value })
    .then(response => {
      if (response.data.success) {
        searchResults.value = response.data.list;
        Swal.fire({
          icon: "success",
          text: response.data.message,
        });
      } else {
        searchResults.value = [];
        Swal.fire({
          icon: "info",
          text: response.data.message,
        });
      }
    })
    .catch(error => {
      if (error.response && error.response.status === 500) {
        Swal.fire({
          icon: "error",
          text: "伺服器錯誤，請稍後再試。",
        });
      } else {
        Swal.fire({
          icon: "error",
          text: "搜尋錯誤：" + error.message,
        });
      }
    });
}

function loadMyGroups() {
  Swal.fire({
    text: "加載中...",
    allowOutsideClick: false,
    showConfirmButton: false,
  });

  axiosapi.defaults.headers['session-id'] = sessionStorage.getItem("Session-Id");
  axiosapi.post(`/ajax/pages/Group/myGroup`)
    .then(response => {
      if (response.data.success) {
        const groups = response.data.message.allowList;

        // 使用 Promise.all 等待所有管理者檢查完成
        Promise.all(groups.map(group =>
          checkIfAdmin(group.groupId).then(isAdmin => {
            group.isAdmin = isAdmin;
          })
        ))
          .then(() => {
            publicGroups.value = groups.filter(group => group.status === 'PUBLIC');
            privateGroups.value = groups.filter(group => group.status === 'PRIVATE');
            friendsGroups.value = groups.filter(group => group.status === 'FRIENDS');
            Swal.close();
          });
      } else {
        Swal.fire({
          icon: "info",
          text: response.data.message,
        });
      }
    })
    .catch(error => {
      Swal.fire({
        icon: "error",
        text: "加載錯誤：" + error.message,
      });
    });
}

function joinGroup(groupId) {
  Swal.fire({
    text: "加入群組中...",
    allowOutsideClick: false,
    showConfirmButton: false,
  });
  isMemberVisible.value = true;
  axiosapi.defaults.headers['session-id'] = sessionStorage.getItem("Session-Id");
  axiosapi.post(`/ajax/pages/Gmembers/add`, { id: groupId })
    .then(response => {
      if (response.data.success) {
        Swal.fire({
          icon: "success",
          text: response.data.message,
        });
        loadMyGroups();
      } else {
        Swal.fire({
          icon: "info",
          text: response.data.message,
          showCancelButton: true,
          confirmButtonText: '申請',
          cancelButtonText: '關閉',
        }).then(result => {
          if (result.isConfirmed) {
            // 用戶點申請
            console.log(groupId);
            axiosapi.defaults.headers['session-id'] = sessionStorage.getItem("Session-Id");
            axiosapi.post(`/ajax/pages/private/check`, { id: groupId })
              .then(response => {
                if (response.data.success) {
                  Swal.fire({
                    icon: "success",
                    text: response.data.message,
                  });
                } else {
                  Swal.fire({
                    icon: "info",
                    text: response.data.message,
                  });
                }
              })
          }
        })

      }
    })
    .catch(error => {
      Swal.fire({
        icon: "error",
        text: "加入錯誤：" + error.message,
      });
    });
}

// 驗證是否為有效的 JSON
function isValidJson(jsonString) {
  try {
    JSON.parse(jsonString);
    return true;
  } catch (e) {
    return false;
  }
}

const toggleChatRoom = () => {
  if (isChatRoomVisible.value) {
    // 當聊天室顯示時，斷開 WebSocket 連接
    disconnect();
  }
  isChatRoomVisible.value = !isChatRoomVisible.value;
};

// 滾動到聊天內容的最底部
const scrollToBottom = () => {
  nextTick(() => {
    const chatBox = document.getElementById('chat');
    if (chatBox) {
      chatBox.scrollTop = chatBox.scrollHeight;
    }
  });
};

// 連接 WebSocket
function connect(groupId) {
  const sessionId = sessionStorage.getItem("Session-Id");
  if (!sessionId) {
    alert('Session-Id 不存在');
    return;
  }
  // 192.168.23.59
  //localhost
  ws.value = new WebSocket(`ws://192.168.23.59:8080/chat?groupId=${groupId}&session-id=${sessionId}`);

  ws.value.onopen = () => {
    isChatRoomVisible.value = true;
    // 連接成功後，獲取先前的消息
    axiosapi.post(`/ajax/pages/Message/findtmesges`, { groupId })
      .then(response => {
        chat.value = response.data.messages.map(msg => ({
          ...msg,
          image: msg.image ? 'data:image/png;base64,' + msg.image : null,
          memberImage: msg.memberImage ? 'data:image/png;base64,' + msg.memberImage : null
        }));
        scrollToBottom();
      })
      .catch(error => console.error('Error fetching messages:', error));
  };

  ws.value.onmessage = event => {
    if (isValidJson(event.data)) {
      const data = JSON.parse(event.data);
      chat.value.push({
        ...data,
        image: data.image ? 'data:image/png;base64,' + data.image : null,
        memberImage: data.memberImage ? 'data:image/png;base64,' + data.memberImage : null
      });
      scrollToBottom();
    } else {
      console.error("Error parsing JSON: ", event.data);
    }
  };

  ws.value.onclose = () => {
    console.log("WebSocket closed.");
    ws.value = null;
    isChatRoomVisible.value = false;
  };
}

// 斷開 WebSocket 連線
function disconnect() {
  if (ws.value) {
    ws.value.close();
  }
}

// 發送消息
function sendMessage() {
  if (!ws.value || ws.value.readyState !== WebSocket.OPEN) {
    console.error("WebSocket connection is not open.");
    return;
  }

  const groupId = new URL(ws.value.url).searchParams.get('groupId'); // 從 WebSocket URL 獲取 groupId
  const file = imageInput.value.files[0];

  if (!messageInput.value && !file) {
    Swal.fire({
      icon: "warning",
      text: "消息內容不能為空。",
    });
    return;
  }

  let chatMessage = {
    groupId: groupId,
    content: messageInput.value || "", // 確保 content 至少是空字串
    sender_id: sessionStorage.getItem("Session-Id") || "default-id" // 使用 Session-Id
  };

  if (file) {
    const reader = new FileReader();
    reader.onload = function (event) {
      chatMessage.image = event.target.result.split(',')[1]; // 取得 Base64 字符串
      axiosapi.post(`/ajax/pages/Message/tmesges`, chatMessage)
        .then(response => {
          if (response.data.success) {
            chatMessage = {
              ...chatMessage,
              memberName: response.data.success.memberName,
              memberImage: response.data.success.memberImage
            };
            ws.value.send(JSON.stringify(chatMessage));
            messageInput.value = ''; // 清空輸入框
            imageInput.value.value = ''; // 清空文件選擇框
          } else {
            console.error('Error sending message:', response.data.message);
          }
        })
        .catch(error => console.error('Error sending message:', error));
    };
    reader.readAsDataURL(file);
  } else {
    chatMessage.image = ""; // 沒有圖片時，確保 image 是空字串
    axiosapi.post(`/ajax/pages/Message/tmesges`, chatMessage)
      .then(response => {
        if (response.data.success) {
          chatMessage = {
            ...chatMessage,
            memberName: response.data.success.memberName,
            memberImage: response.data.success.memberImage
          };
          ws.value.send(JSON.stringify(chatMessage));
          messageInput.value = ''; // 清空輸入框
        } else {
          console.error('Error sending message:', response.data.message);
        }
      })
      .catch(error => console.error('Error sending message:', error));
  }
}

function leaveGroup(groupId) {
  Swal.fire({
    text: "離開群組中...",
    allowOutsideClick: false,
    showConfirmButton: false,
  });
  console.log(groupId);
  axiosapi.defaults.headers['session-id'] = sessionStorage.getItem("Session-Id");
  axiosapi.delete(`/ajax/pages/Gmembers/leave/` + groupId)
    .then(response => {
      if (response.data.success) {
        Swal.fire({
          icon: "success",
          text: response.data.message,
        });
        // 請不要再亂動了
        let goid = response.data.message;
        loadMyGroups(goid);
        // 這是必要的傳入
      } else {
        Swal.fire({
          icon: "warning",
          text: response.data.message,
        });
      }
    }).catch(error => {
      Swal.fire({
        icon: "error",
        text: "離開錯誤：" + error.message,
      });
    }

    );
}

// /Group/manage
function manageGroup(groupId) {
  axiosapi.defaults.headers['session-id'] = sessionStorage.getItem("Session-Id");
  axiosapi.get(`/ajax/pages/Group/myGroup/memberlist/` + groupId)
    .then(response => {
      if (response.data.success) {
        isManageVisible.value = true;

        // 解析回傳的 JSON 並設置 manageGroups
        const memberlist = JSON.parse(response.data.message).memberlist;
        manageGroups.value = memberlist;
      } else {
        Swal.fire({
          icon: "info",
          text: response.data.message,
        });
      }
    })
    .catch(error => {
      Swal.fire({
        icon: "error",
        text: "管理群組錯誤：" + error.message,
      });
    });
}

// /Group/readyallow
function readyallow(groupId) {
  axiosapi.defaults.headers['session-id'] = sessionStorage.getItem("Session-Id");
  axiosapi.post(`/ajax/pages/Grtes/ttst2`, { id: groupId })
    .then(response => {
      if (response.data.success) {
        isManageVisible.value = true;

        // 解析回傳的 JSON 並設置 manageGroups
        const allowList = JSON.parse(response.data.message).allowList;
        manageGroups.value = allowList;
      } else {
        Swal.fire({
          icon: "info",
          text: response.data.message,
        });
      }
    })
    .catch(error => {
      Swal.fire({
        icon: "error",
        text: "管理群組錯誤：" + error.message,
      });
    });
}

function approveGroup(member) {
  // 這裡的 body 是需要提交的數據
  const requestBody = {
    allowList: [
      {
        isCheck: false, // 可以根據實際情況修改這個值
        userId: member.userId, // 替換為實際的 userId
        memberId: member.memberId, // 或者其他合適的值
        groupId: member.groupId
      }
      // 如果有多個項目，可以在這裡添加更多
    ]
  };

  Swal.fire({
    text: "正在審核中...",
    allowOutsideClick: false,
    showConfirmButton: false,
  });

  axiosapi.post('/ajax/pages/Grtes/ttst3', requestBody)
    .then(response => {
      if (response.data.success) {
        Swal.fire({
          icon: "success",
          text: "審核成功",
        });
        readyallow(response.data.message);

        // 刷新或更新管理群組的資料


      } else {
        Swal.fire({
          icon: "warning",
          text: response.data.message || "審核失敗",
        });
      }
    })
    .catch(error => {
      Swal.fire({
        icon: "error",
        text: "審核錯誤：" + (error.response && error.response.data.message ? error.response.data.message : error.message),
      });
    });
}

function removeMemeber(member) {
  // 這裡的 body 是需要提交的數據

  const requestBody = {
    id: member.groupId, // 群組id
    gid: member.userId // 或者其他合適的值
  };
  Swal.fire({
    text: "正在移除中...",
    allowOutsideClick: false,
    showConfirmButton: false,
  });
  // 管理者id
  axiosapi.defaults.headers['session-id'] = sessionStorage.getItem("Session-Id");
  axiosapi.post('/ajax/pages/Group/myGroup/memberemove', requestBody)
    .then(response => {
      if (response.data.success) {
        Swal.fire({
          icon: "success",
          text: "成員移除成功",
        });
        // 刷新或更新管理群組的資料

        manageGroup(member.groupId);

      } else {
        Swal.fire({
          icon: "warning",
          text: response.data.message || "移除失敗",
        });
      }
    })
    .catch(error => {
      Swal.fire({
        icon: "error",
        text: "移除錯誤：" + (error.response && error.response.data.message ? error.response.data.message : error.message),
      });
    });
}


onMounted(() => {
  loadMyGroups();
});
</script>

<style scoped>
/* 限制圖片大小的 CSS */
.img-limited {
  max-width: 50%;
  max-height: 150px;
  /* 可根據需要調整大小 */
  object-fit: cover;
  /* 確保圖片比例 */
}

/* 聊天室 訊息 配置切版 */
.message-container {
  display: flex;
  align-items: flex-start;
  /* 信息在左，消息内容在右 */
  width: 100%;
  /* 容器占据整个可用宽度 */
}

.member-info {
  display: flex;
  flex-direction: column;
  /* 垂直排列 */
  align-items: center;
  /* 水平居中 */
  margin-right: 10px;
  /* 右边距 */
}

.message-content {
  display: flex;
  flex-direction: column;
  /* 垂直排列消息内容 */
}

.chat-message {
  display: flex;
  padding: 10px;
  margin: 5px 0;
  border-radius: 10px;
  max-width: 70%;
  word-wrap: break-word;
  /* 换行 */
  overflow-wrap: break-word;
  /* 确保换行 */
}

.chat-message.self {
  align-self: flex-end;
  background-color: #dd9fbc;
  /* 可以根据需要调整颜色 */
}

.chat-message:not(.self) {
  align-self: flex-start;
  background-color: #93ceb0;
  /* 可以根据需要调整颜色 */
}

.chat-image {
  max-width: 500px;
  /* 根据需要调整 */
  max-height: 500px;
  /* 根据需要调整 */
  margin-top: 5px;
}


/* 新增加入的成員 */
.joinmember-card {
  display: flex;
  flex-direction: column;
  align-items: start;
  /* 項目從左側對齊 */
  gap: 10px;
  /* 控制項目之間的間距 */
  padding: 15px;
  /* 內部填充 */
  border: 1px solid #ddd;
  /* 邊框 */
  border-radius: 5px;
  /* 圓角 */
  background-color: #f9f9f9;
  /* 背景顏色 */
}

.joinmember-card p {
  margin: 0;
  /* 移除段落的外部邊距 */
}

.joinmember-card input,
.joinmember-card button {
  width: 100%;
  /* 讓輸入框和按鈕填滿容器寬度 */
}

/* 新增加入的成員^^^ */




input,
select,
button {
  margin-right: 10px;
  margin-bottom: 10px;
  padding: 5px 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 14px;
}

.group-box,
.group-item {
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 10px;
  margin-bottom: 10px;
  background-color: #f9f9f9;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.group-image,
.group-no-image {
  width: 50px;
  height: 50px;
  object-fit: cover;
  margin-right: 10px;
}

.group-no-image {
  background-color: #ddd;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #999;
}

.group-info {
  display: flex;
  flex-direction: column;
}

.group-category {
  cursor: pointer;
  font-weight: bold;
  margin: 10px 0;
}

.chat-room-popup {
  position: fixed;
  bottom: 20px;
  /* 將窗口固定在頁面的底部 */
  right: 20px;
  /* 距離右邊的距離 */
  width: 1500px;
  /* 設定固定寬度 */
  max-height: 100vh;
  /* 最大高度設置為視口高度的80% */
  overflow: auto;
  /* 當內容超過彈出窗口時，顯示滾動條 */
  background: white;
  border: 1px solid #ccc;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  z-index: 1000;
  /* 確保彈出窗口在最上層 */
}

.chat-room-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px;
  border-bottom: 1px solid #ddd;
  background: #f5f5f5;
}

.close-button {
  background: red;
  color: white;
  border: none;
  border-radius: 50%;
  width: 30px;
  /* 圓圈的直徑 */
  height: 30px;
  /* 圓圈的直徑 */
  text-align: center;
  line-height: 30px;
  /* 垂直對齊文本 */
  font-size: 18px;
  /* 調整字體大小使X顯示清晰 */
  cursor: pointer;
  display: inline-flex;
  /* 使用 inline-flex 確保按鈕在容器中對齊 */
  align-items: center;
  /* 垂直對齊內容 */
  justify-content: center;
  /* 水平對齊內容 */
}

.close-button:hover {
  background: darkred;
}

.chat-box {
  display: flex;
  flex-direction: column;
  height: 600px;
  /* 調整高度以符合需求 */
  overflow-y: scroll;
}

/* 訊息測試 */
.chat-message {
  display: flex;
  align-items: flex-start;
  /* 垂直對齊內容，成員信息在左邊 */
  padding: 10px;
  margin: 5px 0;
  border-radius: 10px;
  max-width: 70%;
  word-wrap: break-word;
  /* 确保长单词会换行 */
  overflow-wrap: break-word;
  /* 处理文本换行 */
}

.chat-message.self {
  align-self: flex-end;
  background-color: #eaebe5;
  /* 可以根據需要調整顏色 */
}

.chat-message:not(.self) {
  align-self: flex-start;
  background-color: #da5050e7;
  /* 可以根據需要調整顏色 */
}

.chat-image {
  max-width: 500px;
  /* 根據需要調整 */
  max-height: 500px;
  /* 根據需要調整 */
  margin-top: 5px;
}

/* 訊息測試 */

button {
  cursor: pointer;
}

.chat-inputs {
  display: flex;
  align-items: center;
}

.chat-inputs input[type="text"] {
  flex: 1;
  margin-right: 10px;
}


button:hover {
  background-color: #eee;
}

/* 聊天室內成員圖片 */
.member-info {
  display: flex;
  flex-direction: column;
  /* 改變排列方向為垂直方向 */
  align-items: center;
  /* 使內容在橫向居中對齊 */
  margin-bottom: 5px;
  /* 控制名稱和訊息之間的間距 */
}

.member-image {
  width: 40px;
  /* 調整成員圖片大小 */
  height: 40px;
  /* 調整成員圖片大小 */
  border-radius: 50%;
  object-fit: cover;
  margin-right: 10px;
}

.member-name {
  font-weight: bold;
  margin-right: 10px;
}

/* 管理群組測條 */
#sidebar {
  position: fixed;
  top: 300px;
  right: 20px;
  width: 300px;
  max-height: 400px;
  /* 设置最大高度 */
  overflow-y: auto;
  /* 启用垂直滚动条 */
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 10px;
  background-color: #f9f9f9;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

#sidebar ul {
  list-style-type: none;
  padding: 0;
}

#sidebar li {
  margin-bottom: 10px;
}

/* 管理群組的X關閉按鈕 */
.manage-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.close-button {
  background: none;
  border: none;
  font-size: 1.5rem;
  cursor: pointer;
}

/* 管理部分 加上 成員卡 圖片及成員名稱 */
.member-list {
  list-style: none;
  padding: 0;
}

.member-card {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 10px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.member-info {
  display: flex;
  align-items: center;
}

.member-image {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  object-fit: cover;
  margin-right: 10px;
}

.member-details {
  display: flex;
  flex-direction: column;
}

.member-actions {
  margin-left: auto;
  display: flex;
  gap: 10px;
}
</style>