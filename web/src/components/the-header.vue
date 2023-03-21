<template>
  <a-layout-header class="header">
    <div class="header-menu">
      <a-menu
          theme="light"
          mode="horizontal"
          class="a-menu"
      >
        <a-menu-item key="1">
          <router-link to="/Home">首页</router-link>
        </a-menu-item>
        <a-menu-item key="2">
          <router-link to="/download/Download">下载</router-link>
        </a-menu-item>
        <a-menu-item key="3">
          <router-link to="/course/Course">课程</router-link>
        </a-menu-item>
        <a-menu-item key="8">
          <router-link to="/column/Column">专栏</router-link>
        </a-menu-item>
        <a-menu-item key="4">
          <router-link to="/admin/AdminDownload">下载管理</router-link>
        </a-menu-item>
        <a-menu-item key="5">
          <router-link to="/admin/AdminCourse">课程及视频管理</router-link>
        </a-menu-item>
        <a-menu-item key="6">
          <router-link to="/admin/AdminCategory">分类管理</router-link>
        </a-menu-item>
        <a-menu-item key="9">
          <router-link to="/admin/AdminColumn">专栏管理</router-link>
        </a-menu-item>
        <a-menu-item key="7">
          <router-link to="/About">关于</router-link>
        </a-menu-item>
      </a-menu>

    </div>
    <div class="login-menu" @click="loginClick">
      <a>登录</a>
    </div>
  </a-layout-header>

  <a-modal
      title="登录"
      v-model:visible="loginModalVisible"
      :confirm-loading="loginModalLoading"
      @ok="loginModalOk"
  >
    <a-form
        :model="userInModal"
        :label-col="{ span : 2 }"
    >
      <a-form-item label="账号">
        <a-input v-model:value="userInModal.useraccount"/>
      </a-form-item>
      <a-form-item label="密码">
        <a-input v-model:value="userInModal.password"/>
      </a-form-item>
      <div style="text-align: center">其它方式登录</div>
      <div
          style="padding-left: 20px"
          @click="loginByGitHub"
      >
        <img src="../assets/githubIcon.jpg" alt="githubIcon" style="width: 25px">
        <a>github 登录</a>
      </div>
    </a-form>
    <template #footer>
      <div class="modal-footer-div">
        <a-button key="submit" type="primary"  @click="loginModalOk" style="width: 30%">登录</a-button>
      </div>
    </template>
  </a-modal>

</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from 'vue';
import axios from "axios";
import {message} from "ant-design-vue";

export default defineComponent({
  name: 'the-header',
  // props: {
  //   msg: String,
  // } // 父子组件间传递数据
  setup: function () {

    const loginModalVisible = ref(false);
    const loginModalLoading = ref(false);
    const userInModal = ref();
    userInModal.value = [];

    const loginClick = () => {
      loginModalVisible.value = true;
      console.log("lala");
    }

    const loginModalOk = () => {
      loginModalVisible.value = false;
      console.log("lala");

    }

    const loginByGitHub = () => {
      window.open("http://124.223.184.187:8111/user/render")
      // axios.get("/user/render").then((response) => {
      //
      //   if (response.data.success) {  // 判断后端接口返回是否出错
      //     console.log("render success");
      //   } else {
      //     message.error(response.data.message);
      //   }
      // })
    }

    onMounted(() => {
      console.log("onMounted");
    });

    return {
      loginClick,
      loginModalVisible,
      loginModalLoading,
      loginModalOk,
      userInModal,
      loginByGitHub
    };
  }
});
</script>

<style>

.header {
  color: white !important;
  background: white !important;
  /*text-align: center !important;*/

}

.header-menu {
  display: inline-block;
  width: 85%;
  padding-left: 20%;
}

.login-menu {
  color: black;
  width: 15%;
  float: right;
  /*font-weight: bold;*/
}

.modal-footer-div {
  text-align: center;
}
</style>