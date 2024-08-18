<template>
<div  ref="exampleRef" class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">訂閱項目</h5>
                    <div class="container group">
                        <FindSubsCompo2 v-for="product in products" :key="product.ServiceId" :productA="product" :showbtn @customDelete="callRemove" @customAdd="callAdd" @customCancel="callCancel"></FindSubsCompo2>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import FindSubsCompo2 from '@/components/Subscriptions/FindSubsCompo2.vue'
import axiosapi from '@/plugins/axios.js';
import Swal from 'sweetalert2';
import { ref, onMounted } from 'vue';
import bootstrap from "bootstrap/dist/js/bootstrap.bundle.min.js"
const products = ref([]);
const showbtn = ref("");
const exampleRef = ref(null);
const exampleModal = ref(null);

    onMounted(function() {
        exampleModal.value = new bootstrap.Modal(exampleRef.value);
        callFind();
    });

    function showModal() {
        exampleModal.value.show();
    }
    function hideModal() {
        exampleModal.value.hide();
    }

    defineExpose({
        showModal, hideModal,
    });


function callFind() {
// let request = {
//   "start": start.value,
//   "max": rows.value,
//   "dir": false,
//   "order": "price",
// };
axiosapi.defaults.headers['session-id'] = sessionStorage.getItem("Session-Id");
axiosapi.post("/ajax/pages/servicesInfo/findMyCreate").then(function (response) {
  console.log("response:", response);
  products.value = response.data.ServList;
  showbtn.value = "findMyCreate";
}).catch(function (error) {
  console.log("error", error);
  Swal.fire({
      text: "查詢失敗：" + error.message,
      icon: "error"
  });
});
}

function callRemove(id) {
Swal.fire({
  icon: 'question',
  text: '確定要刪除？',
  showCancelButton: true,
  allowOutsideClick: false
}).then(function (result) {
  if (result.isConfirmed) {
      Swal.fire({
          text: "執行中......",
          allowOutsideClick: false,
          showConfirmButton: false,
      });
      if (id) {
          axiosapi.delete("/ajax/pages/servicesInfo/del/" + id).then(function (response) {
              console.log("response", response);
              if (response.data.success) {
                  Swal.fire({
                      icon: "success",
                      text: response.data.message,
                  }).then(function (result) {
                      if (lastPageRows.value == 1 && current.value > 1) {
                          current.value = current.value - 1;
                      }
                      callFind(current.value);
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
                  text: "刪除錯誤：" + error.message,
              });
          });
      }
  }
});
}

function callCancel(id) {
Swal.fire({
  icon: 'question',
  text: '確定要取消訂閱嗎？',
  showCancelButton: true,
  allowOutsideClick: false
}).then(function (result) {
  if (result.isConfirmed) {
      Swal.fire({
          text: "執行中......",
          allowOutsideClick: false,
          showConfirmButton: false,
      });
      if (id) {
          axiosapi.defaults.headers['session-id'] = sessionStorage.getItem("Session-Id");
          axiosapi.delete("/ajax/pages/SubServices/del/" + id).then(function (response) {
              console.log("response", response);
              if (response.data.success) {
                  Swal.fire({
                      icon: "success",
                      text: response.data.message,
                  }).then(function (result) {
                      if (lastPageRows.value == 1 && current.value > 1) {
                          current.value = current.value - 1;
                      }
                      callFind(current.value);
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
                  text: "刪除錯誤：" + error.message,
              });
          });
      }
  }
});
}

function callAdd(id) {
    Swal.fire({
        text: "訂閱中......",
        allowOutsideClick: false,
        showConfirmButton: false,
    });
    let request = {
        "servInfoId": id
    };
    axiosapi.defaults.headers['session-id'] = sessionStorage.getItem("Session-Id");
    console.log(["id", id],["session-id", axiosapi.defaults.headers['session-id']]);
    axiosapi.post("/ajax/pages/SubServices/add", request).then(function (response) {
        console.log("response", response);
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
    }).catch(function (error) {
        console.log("error", error);
        Swal.fire({
            icon: "error",
            text: "新增錯誤：" + error.message,
        });
    });
}
</script>

<style scoped>

</style>