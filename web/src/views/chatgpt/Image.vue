<template>
  <a-layout-content class="layout-content">

    <a-button type="primary" @click="showAttention" class="show-attention-button">注<br>意<br></a-button>

    <a-input-search
        class="input-search"
        v-model:value="imagePrompt"
        placeholder="描述一下你想要的图片呗, 例如: 一只灰色的, 会跳舞的猫"
        :loading="searchLoading"
        enter-button
        @search="onSearch"
    />

    <a-spin
        class="spin"
        tip="加载过程比较慢, 请耐心等待..."
        :spinning="spanning">
    </a-spin>

    <div class="show-image" v-if="ifShowImage">
      <a-image
          :width="500"
          :src="href"
      />
      <br><br>
      <!--      <div>-->
      <!--        图片链接: {{href}}-->
      <!--      </div>-->
    </div>
  </a-layout-content>


</template>

<script>
import {computed, defineComponent, onMounted, ref, h} from "vue";
import {Tool} from "@/utils/tool";
import {message, Modal} from "ant-design-vue";
import axios from "axios";
import store from "@/store";

export default defineComponent({
  name: "image.vue",
  setup() {

    const imagePrompt = ref("");
    const searchLoading = ref(false);
    const href = ref();
    const spanning = ref(false);
    const ifShowImage = ref(false);
    const imageCost = 20;


    const userInfo = computed(() => {
      return store.state.userInfo;
    });

    const onSearch = () => {
      if (Tool.isEmpty(userInfo.value)) {           // 检测是否登录
        message.warn("需要先登录才能用呦~~~");
        return;
      }

      ifShowImage.value = false;
      searchLoading.value = true;
      href.value = "";
      // const imagePromptJSON = JSON.stringify(imagePrompt.value);
      spanning.value = true;

      axios.post(process.env.VUE_APP_LOCAL_GPT_TEST + "/gpt/image/"
          + btoa(encodeURIComponent(imagePrompt.value))
          + "&"
          + userInfo.value.id
      ).then((response) => {
        imagePrompt.value = "";
        searchLoading.value = false;
        spanning.value = false;
        ifShowImage.value = true;

        if (response.data.success) {
          href.value = response.data.content;

        } else {
          message.error(response.data.message);
        }
      })


    }

    const showAttention = () => {
      Modal.info({
        title: '注意',
        content: h('div', {}, [
          h('p', '目前画图只能「会员」使用嘞'),
          h('p', '每个生成图片的链接有效期只有 24 小时'),
          h('p', '为了提高图片加载速度, 图片质量为 512x512'),
          h('p', '本模块没有 "历史记录", 满足要求的图片要及时保存呦'),
        ]),
        width: 500,
        okText: '了然'
      });
    }


    onMounted(() => {
      // console.log("aa");
    })

    return {
      imagePrompt,
      searchLoading,

      onSearch,
      showAttention,
      href,
      spanning,
      ifShowImage,
    }
  }
})
</script>

<style scoped>

@media (max-width: 768px) {

  .layout-content {
    width: 87% !important;
    margin-right: 5px !important;
  }

  .show-attention-button {
    transform: scale(0.7);
    left: 0px !important;
    top: 235px !important;
  }

  .input-search {
    transform: scale(0.9);
    position: fixed;
    width: 96.5% !important;
    left: 7% !important;
    top: 8%;
  }

  .show-attention-button {
    position: fixed;
    width: 40px;
    height: 70px;
    left: 8px;
    top: 10% !important;
  }
}

.layout-content {
  padding-top: 60px;
  width: 85%;
  height: 100%;
  min-height: 1100px;
  margin: 10px auto 100px;
  overflow: hidden;
  background: #fff;
}

.show-image {
  padding-top: 10%;
  text-align: center;
}

.spin {
  padding-top: 10%;
  padding-left: 40%;
}

.show-attention-button {
  position: fixed;
  width: 40px;
  height: 70px;
  left: 8px;
  top: 10%;
}
</style>