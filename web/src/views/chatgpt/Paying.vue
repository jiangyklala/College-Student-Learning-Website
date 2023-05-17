<template>
  <a-layout-content class="layout-content">
    <a-card :body-style="{ display: 'flex', flexDirection: 'column' }" style="width: 100%">
      <p style="float: left; color: rgb(156, 160, 164)">请在 5 分钟内完成付款: </p>
      <br>
      <br>
      <div ref="qrcode"></div>
    </a-card>
  </a-layout-content>
</template>

<script lang="ts">
import {computed, defineComponent, onMounted, ref} from "vue";
import {useRoute} from "vue-router";
import {Tool} from "@/utils/tool";
import axios from "axios";
import QRCode from "qrcodejs2";
import {message} from "ant-design-vue";
import store from "@/store";
import router from "@/router";


export default defineComponent ({
  name: "Paying",
  setup () {

    const qrcode = ref();
    const orderId = ref();
    const chooseValue = ref();

    const userInfo = computed(() => {
      return store.state.userInfo;
    });

    const showQR = () => {

      if (Tool.isNotEmpty(qrcode.value)) {
        qrcode.value.innerHTML = '';
      }

      axios.post(process.env.VUE_APP_SERVER + "/pay/vipPayWith", {
        num: chooseValue.value,
        userId : userInfo.value.id,
      }).then((response) => {
        if (response.data.success) {
          orderId.value = response.data.content.orderId;
          const qrcodeResp = new QRCode(qrcode.value, {
            text: response.data.content.codeUrl, // 二维码内容
            width: 200, // 二维码宽度
            height: 200, // 二维码高度
            colorDark : '#000000', // 二维码颜色
            colorLight : '#ffffff', // 二维码背景色
          });

          console.log(qrcodeResp);
        } else {
          message.error(response.data.message);
        }
      })
    }

    const setTimer = () => {
      const payWithTimerId = setInterval(() => {
        console.log("create");
        axios.post(process.env.VUE_APP_SERVER + "/pay/checkIfPay", {
          orderId: orderId.value,
        }).then((response) => {
          if (response.data.success) {
            if (response.data.content.success) {
              console.log("success");
              console.log(chooseValue.value);

              axios.post(process.env.VUE_APP_SERVER + "/pay/vipToUser", {
                userId: userInfo.value.id,
                num: chooseValue.value,
              }).then((response) => {
                if (response.data.success) {
                  message.success("充值成功!");
                  userInfoRefresh();
                  goToChatPage();
                } else {
                  message.error(response.data.message);
                }
              })

              clearInterval(payWithTimerId);
            }
          } else {
            message.error(response.data.message);
          }
        })
      }, 2000);

      const destroyPayWithTimerId = setTimeout(() => {
        console.log("destroy");
        clearTimeout(payWithTimerId);
        clearTimeout(destroyPayWithTimerId);
      }, 5 * 60 * 1000);
    }

    const goToChatPage = () => {
      console.log(chooseValue);
      router.push({
        name: 'Chatgpt',
      });
    };

    const userInfoRefresh = () => {
      axios.defaults.withCredentials = true;
      axios.post("/user/loginByID/" + userInfo.value.id).then((response) => {
        if (response.data.success) {   // 成功则加载用户信息
          store.commit("setUserInfo", response.data.content);
        } else {
          message.error(response.data.message);
        }
      })
    }

    onMounted(() => {
      const route = useRoute();
      chooseValue.value = route.params.chooseValue;
      showQR();
      setTimer();
    })

    return {
      qrcode,
    }
  }

})
</script>

<style scoped>
.layout-content {
  padding-top: 60px;
  width: 80%;
  height: 100%;
  min-height: 200px;
  margin: 20px auto 100px;
  overflow: hidden;
  background: rgb(237, 239, 242);
}
</style>