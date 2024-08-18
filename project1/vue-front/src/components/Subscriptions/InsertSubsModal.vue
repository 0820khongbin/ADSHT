<template>
    <div ref="exampleRef" class="modal fade" id="staticBackdrop" tabindex="-1" aria-labelledby="staticBackdropLabel"
        data-bs-backdrop="static" data-bs-keyboard="false" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">

                <div class="modal-header">
                    <h1 class="modal-title fs-5" id="staticBackdropLabel">新增您的會員服務</h1>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>

                <div class="modal-body">
                    <div class="container group">


                        <form @submit.prevent="handleSubmit" class="subscription-form">
                            <div class="form-group">
                                <label for="Name">服務名稱:</label>
                                <input type="text" name="serviceName" v-model="serviceName" />
                            </div>

                            <div class="form-group">
                                <label for="servicePrice">價格:</label>
                                <input type="number" name="servicePrice" v-model="servicePrice" />
                            </div>

                            <div class="form-group">
                                <label for="serviceDep">服務內容:</label>
                                <input type="text" name="serviceDep" v-model="serviceDep" />
                            </div>

                            <div class="form-group">
                                <label for="serviceLevel">會員等級:</label>
                                <!-- <input type="text" name="serviceLevel" v-model="serviceLevel" /> -->
                                <div class="btn-group" role="group" aria-label="Basic radio toggle button group">
                                    <input type="radio" class="btn-check" id="btnradio1" value="1" name="serviceLevel"
                                        autocomplete="off" v-model="serviceLevel">
                                    <label class="btn btn-outline-primary" for="btnradio1">等級 1</label>

                                    <input type="radio" class="btn-check" id="btnradio2" value="2" name="serviceLevel"
                                        autocomplete="off" v-model="serviceLevel">
                                    <label class="btn btn-outline-primary" for="btnradio2">等級 2</label>

                                    <input type="radio" class="btn-check" id="btnradio3" value="3" name="serviceLevel"
                                        autocomplete="off" v-model="serviceLevel">
                                    <label class="btn btn-outline-primary" for="btnradio3">等級 3</label>

                                    <input type="radio" class="btn-check" id="btnradio4" value="4" name="serviceLevel"
                                        autocomplete="off" v-model="serviceLevel">
                                    <label class="btn btn-outline-primary" for="btnradio4">等級 4</label>

                                    <input type="radio" class="btn-check" id="btnradio5" value="5" name="serviceLevel"
                                        autocomplete="off" v-model="serviceLevel">
                                    <label class="btn btn-outline-primary" for="btnradio5">等級 5</label>
                                </div>
                            </div>
                            <div class="img-review">
                                <!-- <div class="form-group">
                                <label for="serviceAcitve">是否啟用:</label>
                                <input type="text" name="serviceAcitve" v-model="serviceAcitve" />
                            </div> -->
                                <div class="mb-3">
                                    <span class="icon">
                                        <input class="form-control d-none" type="file" id="postImage"
                                            @change="onFileChange" ref="fileInput">
                                    </span>
                                    <button type="button" class="btn btn-secondary btn-sm"
                                        @click="triggerFileInput">上傳會員圖示
                                        <font-awesome-icon :icon="['fas', 'camera-retro']" class="icon-large"
                                            style="cursor: pointer;" />
                                    </button>
                                </div>
                                <div v-if="serviceImage" class="form-group">
                                    <img :src="serviceImage" alt="Image Preview" class="img-fluid" width="100px"
                                        height="100px">
                                    <font-awesome-icon :icon="['fas', 'trash']" class="icon-large trash-icon"
                                        v-if="serviceImage" @click="clearImage" />
                                </div>
                            </div>
                        </form>



                    </div>
                </div>

                <div class="modal-footer">
                    <div class="mb-3">
                    </div>

                    <button type="submit" class="submit-button" @click="callCreate">送出</button>
                    <!-- <button type="button" class="btn btn-primary" @click="emits('customUpdate')">送出修改</button> -->
                </div>

            </div>
        </div>
    </div>
</template>

<script setup>
const props = defineProps(["modelValue"]);
// const emits = defineEmits(["customCreate"]);
//載入
import Swal from 'sweetalert2';
import axiosapi from '@/plugins/axios.js';
import bootstrap from "bootstrap/dist/js/bootstrap.bundle.min.js"
import { ref, onMounted } from 'vue'

//定義變數
const serviceName = ref("");
const servicePrice = ref("");
const serviceDep = ref("");
const serviceLevel = ref("");
const serviceAcitve = ref("");
const exampleRef = ref(null);
const exampleModal = ref(null);
const serviceImage = ref("");
const fileInput = ref(null);
const levels = [1, 2, 3, 4, 5]



onMounted(function () {
    exampleModal.value = new bootstrap.Modal(exampleRef.value);
});

//監聽輸入
function doinput(key, event) {
    // console.log("event.target.value", event.target.value);
    // console.log("key", key);
    emits('update:modelValue', {
        ...props.modelValue,
        [key]: event.target.value
    });
}

//對資料庫新增資料function
function callCreate() {
    Swal.fire({
        text: "執行中......",
        allowOutsideClick: false,
        showConfirmButton: false,
    });
    console.log("serviceLevel.value", serviceLevel)
    if (serviceName.value == "") {
        serviceName.value = null;
    }
    if (servicePrice.value == "") {
        servicePrice.value = null;
    }
    if (serviceDep.value == "") {
        serviceDep.value = null;
    }
    if (serviceImage.value == "") {
        serviceImage.value = null;
    }
    if (serviceLevel.value == "") {
        serviceLevel.value = null;
    }
    // if (serviceAcitve.value == "") {
    //   serviceAcitve.value = null;
    // }
    let request = {
        "serviceName": serviceName.value,
        "price": servicePrice.value,
        "description": serviceDep.value,
        "level": serviceLevel.value,
        //"acitve": serviceAcitve.value,
        "image": serviceImage.value,
    };

    axiosapi.defaults.headers['session-id'] = sessionStorage.getItem("Session-Id");
    axiosapi.post("/ajax/pages/servicesInfo/add", request).then(function (response) {
        console.log("response", response);
        if (response.data.success) {
            Swal.fire({
                icon: "success",
                text: response.data.message,
            }).then(function (result) {
                serviceName.value = "";
                servicePrice.value = "";
                serviceDep.value = "";
                serviceLevel.value = "";
                serviceImage.value = "";
                hideModal();
            });
        } else {
            Swal.fire({
                icon: "warning",
                text: response.data.message,
            });
        }
    }).catch(function (error) {
        console.log("error", error);
        Swal.fire({
            icon: "error",
            text: "新增錯誤：" + error.message,
        });
    });
}


const onFileChange = (e) => {
    const file = e.target.files[0];
    if (file) {
        const reader = new FileReader();
        reader.onload = (e) => {
            serviceImage.value = e.target.result;
        };
        reader.readAsDataURL(file);
    }
};

const triggerFileInput = () => {
    fileInput.value.click();
};

//清除圖片按鈕
const clearImage = () => {
    serviceImage.value = null;
};

//Modal function
function showModal() {
    exampleModal.value.show();
}
function hideModal() {
    exampleModal.value.hide();
}

defineExpose({
    showModal, hideModal,
});

</script>


<style scoped>
.add-subscription-service {
    max-width: 600px;
    margin: 0 auto;
    padding: 20px;
    border: 1px solid #ccc;
    border-radius: 5px;
    text-align: center;
    font-family: Arial, sans-serif;
}

.header {
    display: flex;
    align-items: center;
    justify-content: center;
    margin-bottom: 20px;
}

.logo {
    width: 50px;
    margin-right: 10px;
}

.subscription-form {
    background-color: #f9f9f9;
    padding: 20px;
    border-radius: 5px;
}

.form-group {
    margin-bottom: 15px;
    text-align: left;
}

.img-review {
    display: flex;
}

.form-group label {
    display: block;
    margin-bottom: 5px;
}

.form-group input,
.form-group select {
    width: 100%;
    padding: 10px;
    box-sizing: border-box;
    border: 1px solid #ccc;
    border-radius: 5px;
}

.submit-button {
    padding: 10px 15px;
    background-color: #ff0000;
    color: #fff;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    margin-top: 10px;
}

.submit-button:hover {
    background-color: #cc0000;
}

.submission-confirmation {
    margin-top: 20px;
    background-color: #e7f4e4;
    padding: 20px;
    border-radius: 5px;
    border: 1px solid #b2d8b2;
}

.icon {
    font-size: 2em;
}


.ui.card.large {
    /* 确保卡片也被调整大小 */
    width: 470px;
    height: auto;
    font-size: 1.3em;
    /* 放大字体 */
}

.ui.avatar.image {
    width: 50px;
    /* 增大头像宽度 */
    height: 50px;
    /* 增大头像高度 */
    object-fit: cover;
    /* 确保头像内容不会变形 */
}

.date-and-menu {
    position: relative;
    /* 确保下拉菜单绝对定位相对于这个容器 */
}

.ui.compact.menu.right.floated {
    position: absolute;
    top: 100px;
    right: 100px;
    border: none;
    /* 移除邊框 */
    box-shadow: none;
    /* 移除陰影 */
    background: none;
    /* 使背景透明 */
    padding: 0;
    font-size: 20px;
}

.postImg {
    width: 100%;
    max-width: 100%;
    height: 500px;
    max-height: 100%;
}

.trash-icon {
    position: relative;
    bottom: -40px;
    right: 0px;
}
</style>