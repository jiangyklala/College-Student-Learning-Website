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
    <div class="login-menu">
      <div v-if="!ifLoginIn">
        <a @click="loginClick">登录</a> | <a @click="registerClick">注册</a>

      </div>
      <a-dropdown v-if="ifLoginIn">
        <a class="ant-dropdown-link" @click.prevent>
          <a-avatar style="background-color: #87d068">
            <template #icon>
              <UserOutlined />
            </template>
          </a-avatar>
          {{ userInfo.username }}
          <DownOutlined />
        </a>
        <template #overlay>
          <a-menu>
            <a-menu-item>
              账户余额: {{userInfo.balance}}
            </a-menu-item>
            <a-menu-item>
              <a>2nd menu item</a>
            </a-menu-item>
            <a-menu-item>
              <a>3rd menu item</a>
            </a-menu-item>
          </a-menu>
        </template>
      </a-dropdown>
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
        <a-input-password v-model:value="userInModal.password"/>
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

  <a-modal
      title="注册"
      v-model:visible="registerModalVisible"
      :confirm-loading="registerModalLoading"
  >
    <a-form
        :model="userInModal"
        :label-col="{ span : 2 }"
    >
      <a-form-item label="账号">
        <a-input v-model:value="userInModal.useraccount"/>
      </a-form-item>
      <a-form-item label="密码">
        <a-input-password v-model:value="userInModal.password"/>
      </a-form-item>
    </a-form>
    <template #footer>
      <div class="modal-footer-div">
        <a-button key="submit" type="primary"  @click="registerModalOk" style="width: 30%">注册</a-button>
      </div>
    </template>
  </a-modal>

</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from 'vue';
import axios from "axios";
import {message} from "ant-design-vue";
import {DownOutlined, UserOutlined} from "@ant-design/icons-vue";

export default defineComponent({
  name: 'the-header',
  components: {
    UserOutlined,
    DownOutlined,
  },
  setup: function () {

    //-------------登录表单--------------
    const loginModalVisible = ref(false);
    const loginModalLoading = ref(false);
    const userInModal = ref();
    userInModal.value = {};

    /**
     * 跳转到 GitHub 登录
     */
    const loginByGitHub = () => {
      window.open("http://124.223.184.187:8111/user/render");
    }

    /**
     * 登录按钮点击
     */
    const loginClick = () => {
      loginModalVisible.value = true;
    }

    /**
     * 登录表单提交
     */
    const loginModalOk = () => {

      console.log(userInModal.value);


      axios.post("/user/loginByAccount", userInModal.value).then((response) => {
        if (response.data.success) {    // 登录成功
          loginModalVisible.value = false;
          location.reload();
        } else {
          message.error(response.data.message);
        }
      })
    }

    //-------------注册表单--------------
    const registerModalVisible = ref(false);
    const registerModalLoading = ref(false);
    /**
     * 注册按钮点击
     */
    const registerClick = () => {
      registerModalVisible.value = true;
    }


    /**
     * 登录表单提交
     */
    const registerModalOk = () => {

      console.log(userInModal.value);


      axios.post("/user/register", userInModal.value).then((response) => {
        if (response.data.success) {    // 注册成功
          message.success(response.data.message);
          registerModalVisible.value = false;
          loginModalVisible.value = true;
        } else {
          message.error(response.data.message);
        }
      })
    }

    //-------------登录/注册标识--------------
    const ifLoginIn = ref(false);
    const userInfo = ref();
    userInfo.value = [];

    /**
     * 自动登录
     */
    const autoLogin = () => {
      axios.defaults.withCredentials = true;
      axios.post("/user/autoLogin").then((response) => {
        console.log(response);

        if (response.data.success) {   // 成功则加载用户信息
          userInfo.value = response.data.content;
          ifLoginIn.value = true;
        }  // 失败无提示
      })
    }

    onMounted(() => {
      autoLogin();             // 页面加载时, 尝试自动登录
    });

    return {
      loginModalVisible,
      loginModalLoading,
      ifLoginIn,

      registerModalVisible,
      registerModalLoading,

      userInModal,
      userInfo,

      loginByGitHub,
      loginModalOk,
      loginClick,
      registerClick,
      registerModalOk,
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