<template>
  <a-layout-content class="layout-content">
    <a-button type="primary" size="middle" @click="showQR">Primary Button</a-button>
    <div ref="qrcode"></div>
  </a-layout-content>

</template>

<script>
import {defineComponent, onMounted, ref} from "vue";
import QRCode from 'qrcodejs2';

export default defineComponent ({
  name: "Pay",
  setup () {

    const qrcode = ref();

    const showQR = () => {
      const qrcodeResp = new QRCode(qrcode.value, {
        text: 'weixin://wxpay/bizpayurl?pr=70XmuWAzz', // 二维码内容
        width: 200, // 二维码宽度
        height: 200, // 二维码高度
        colorDark : '#000000', // 二维码颜色
        colorLight : '#ffffff', // 二维码背景色
      });

      console.log(qrcodeResp);
    }

    onMounted(() => {
      console.log("lala");
    })

    return {
      qrcode,
      showQR,
    };
  }
})
</script>

<style scoped>
.layout-content {
  width: 85%;
  height: 100%;
  min-height: 500px;
  margin: 10px auto 100px;
  overflow: hidden;
  background: #fff;
}
</style>