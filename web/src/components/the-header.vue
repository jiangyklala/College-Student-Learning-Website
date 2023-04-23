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
        <a-menu-item key="10">
          <router-link to="/chatgpt/Chatgpt">GPT 3.5 </router-link>
        </a-menu-item>
        <a-menu-item key="11">
          <router-link to="/chatgpt/Image">3.5 - 画图</router-link>
        </a-menu-item>
<!--        <a-menu-item key="2">-->
<!--          <router-link to="/download/Download">下载</router-link>-->
<!--        </a-menu-item>-->
<!--        <a-menu-item key="3">-->
<!--          <router-link to="/course/Course">课程</router-link>-->
<!--        </a-menu-item>-->
<!--        <a-menu-item key="8">-->
<!--          <router-link to="/column/Column">专栏</router-link>-->
<!--        </a-menu-item>-->
<!--        <a-menu-item key="4">-->
<!--          <router-link to="/admin/AdminDownload">下载管理</router-link>-->
<!--        </a-menu-item>-->
<!--        <a-menu-item key="5">-->
<!--          <router-link to="/admin/AdminCourse">课程及视频管理</router-link>-->
<!--        </a-menu-item>-->
<!--        <a-menu-item key="6">-->
<!--          <router-link to="/admin/AdminCategory">分类管理</router-link>-->
<!--        </a-menu-item>-->
<!--        <a-menu-item key="9">-->
<!--          <router-link to="/admin/AdminColumn">专栏管理</router-link>-->
<!--        </a-menu-item>-->
<!--        <a-menu-item key="7">-->
<!--          <router-link to="/About">关于</router-link>-->
<!--        </a-menu-item>-->
      </a-menu>

    </div>
    <div class="login-menu">
      <div class="login-mark-div" v-if="!ifLoginIn">
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
              剩余提问次数: {{userInfo.balance}}     <sync-outlined @click="userInfoClick" :spin="userInfoRefresh" />
            </a-menu-item>
          </a-menu>
        </template>
      </a-dropdown>
    </div>
  </a-layout-header>

<!--  登录表单-->
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
      <a-form-item label="邮箱">
        <a-input v-model:value="userInModal.email"/>
      </a-form-item>
      <a-form-item label="密码">
        <a-input-password v-model:value="userInModal.password"/>
      </a-form-item>
      <a style="text-align: center; float: right" @click="forgetPasswordHandle">忘记密码?</a>
<!--      <div style="text-align: center">其它方式登录</div>-->
<!--      <div-->
<!--          style="padding-left: 20px"-->
<!--          @click="loginByGitHub"-->
<!--      >-->
<!--        <img src="../assets/githubIcon.jpg" alt="githubIcon" style="width: 25px">-->
<!--        <a>github 登录</a>-->
<!--      </div>-->
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
        :label-col="{ span : 3 }"
    >
      <a-form-item label="邮箱" :required="true">
        <a-input v-model:value="userInModal.email"/>
      </a-form-item>
      <a-form-item label="密码">
        <a-input-password v-model:value="userInModal.password"/>
        <div style="font-size: 10px; color: purple">至少两种字符, 只能包含数字和英文大小写三种字符, 且长度只在 6 - 16 位</div>
      </a-form-item>
      <a-form-item label="验证码">
        <a-input style="width: 70%" v-model:value="userInModal.verifyCode"/><a-button :disabled="verifyBtnDisable" style="float: right; width: 29%" type="dashed" @click="sendVerifyCode">发送验证码</a-button>
      </a-form-item>
      <a-form-item label="邀请码">
        <a-input v-model:value="userInModal.inviteCode"/>
      </a-form-item>
      <a style="text-align: center; float: right;" @click="getInviteCode">如何获取邀请码?</a>
    </a-form>
    <template #footer>
      <div class="modal-footer-div">
        <a-button key="submit" type="primary"  @click="registerModalOk" style="width: 30%">注册</a-button>
      </div>
    </template>
  </a-modal>

  <a-modal
      title="忘记密码"
      v-model:visible="forgetModalVisible"
      :confirm-loading="forgetModalLoading"
  >
    <a-form
        :model="userInModal"
        :label-col="{ span : 4 }"
    >
      <a-form-item label="邮箱" :required="true">
        <a-input v-model:value="userInModal.email"/>
      </a-form-item>
      <a-form-item label="新密码">
        <a-input-password v-model:value="userInModal.password"/>
        <div style="font-size: 10px; color: purple">至少两种字符, 只能包含数字和英文大小写三种字符, 且长度只在 6 - 16 位</div>
      </a-form-item>
      <a-form-item label="重复新密码">
        <a-input-password v-model:value="repeatNewPassword"/>
      </a-form-item>
      <a-form-item label="验证码">
        <a-input style="width: 70%" v-model:value="userInModal.verifyCode"/><a-button :disabled="verifyBtnDisable" style="float: right; width: 29%" type="dashed" @click="sendVerifyCode">发送验证码</a-button>
      </a-form-item>
    </a-form>
    <template #footer>
      <div class="modal-footer-div">
        <a-button key="submit" type="primary"  @click="forgetModalOk" style="width: 30%">OK</a-button>
      </div>
    </template>
  </a-modal>

</template>

<script lang="ts">
import {computed, defineComponent, onMounted, ref} from 'vue';
import axios from "axios";
import {MenuProps, message, notification} from "ant-design-vue";
import {DownOutlined, UserOutlined, SyncOutlined} from "@ant-design/icons-vue";
import store from "@/store";
import {Tool} from "@/utils/tool";
import router from "@/router";

export default defineComponent({
  name: 'the-header',
  components: {
    SyncOutlined,
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
      window.open("http://study.playoffer.cn:8111/user/render");
    }

    /**
     * 登录按钮点击, 弹出登录将框
     */
    const loginClick = () => {
      loginModalVisible.value = true;
    }

    /**
     * 登录表单提交
     */
    const loginModalOk = () => {
      console.log(userInModal.value);
      if (Tool.isEmpty(userInModal.value.email) || Tool.isEmpty(userInModal.value.password)) {
        message.warn("邮箱或密码不能为空呦~");
        return;
      }
      axios.post("/user/loginByEmail", userInModal.value).then((response) => {
        if (response.data.success) {    // 登录成功
          loginModalVisible.value = false;
          window.location.href = process.env.VUE_APP_WEB;
        } else {
          message.error(response.data.message);
        }
      })
    }

    //-------------注册表单--------------
    const registerModalVisible = ref(false);
    const registerModalLoading = ref(false);
    const verifyBtnDisable = ref(false);
    /**
     * 注册按钮点击
     */
    const registerClick = () => {
      registerModalVisible.value = true;
    }

    /**
     * 注册表单提交
     */
    const registerModalOk = () => {
      axios.post("/user/register", userInModal.value).then((response2) => {
        if (response2.data.success) {    // 注册成功
          message.success(response2.data.message + ", 自动跳转到登录页面", 5);
          registerModalVisible.value = false;
          loginModalVisible.value = true;
        } else {
          message.error(response2.data.message);
        }
      })
    }

    /**
     * 发送邮箱验证码
     */
    const sendVerifyCode = () => {
      if (Tool.isEmpty(userInModal.value.email)) {
        message.error("邮箱不能为空呦");
        return;
      }
      verifyBtnDisable.value = true;
      axios.post("/user/sendActiveEmail/" + userInModal.value.email).then((response) => {

        if (response.data.success) {
          message.success("验证码发送成功", 5);
          verifyBtnDisable.value = false;
        } else {
          verifyBtnDisable.value = false;
          message.error(response.data.message);
        }
      })
    }

    const getInviteCode = () => {
      window.open("https://www.playoffer.cn/1619.html");
    }

    //-------------忘记密码--------------
    const forgetModalVisible = ref(false);
    const forgetModalLoading = ref();
    const repeatNewPassword = ref();


    const forgetPasswordHandle = () => {
      forgetModalVisible.value = true;
    }

    const forgetModalOk = () => {
      if (userInModal.value.password != repeatNewPassword.value) {
        message.warn("两次密码不一样呦");
        return;
      }

      axios.post("/user/forget", userInModal.value).then((response2) => {
        if (response2.data.success) {
          message.success(response2.data.message + "更改密码成功, 自动跳转到登录页面", 5);
          forgetModalVisible.value = false;
          loginModalVisible.value = true;
        } else {
          message.error(response2.data.message);
        }
      })
    }

    //-------------登录/注册标识--------------
    const ifLoginIn = ref(false);
    const userInfo = computed(() => {
      return store.state.userInfo;
    })
    const userInfoRefresh = ref(false);

    const userInfoClick = () => {
      userInfoRefresh.value = true;
      axios.defaults.withCredentials = true;
      axios.post("/user/loginByID/" + userInfo.value.id).then((response) => {
        if (response.data.success) {   // 成功则加载用户信息
          store.commit("setUserInfo", response.data.content);
        } else {
          message.error(response.data.message);
        }
        userInfoRefresh.value = false;
      })
    }

    /**
     * 点击右上角的账户详情
     */
    const dropdownClickHandle = () => {
      axios.defaults.withCredentials = true;
      axios.post("/user/loginByID/" + userInfo.value.id).then((response) => {
        if (response.data.success) {   // 成功则加载用户信息
          store.commit("setUserInfo", response.data.content);
        }
      })
    };

    /**
     * 自动登录
     */
    const autoLogin = (userID : string) => {
      axios.defaults.withCredentials = true;
      axios.post("/user/loginByID/" + userID).then((response) => {
        console.log("尝试自动登录");
        // console.log(response);

        if (response.data.success) {   // 成功则加载用户信息
          store.commit("setUserInfo", response.data.content);
          wsLogin();
          ifLoginIn.value = true;
        }  // 失败无提示
      })
    }

    /**
     * 验证登录凭证
     */
    const checkLoginCert = () => {
      axios.defaults.withCredentials = true;
      axios.post("/user/checkLoginCert").then((response) => {        // 验证登录凭证是否有效
        // console.log("验证登录凭证")
        // console.log(response);

        if (response.data.success) {                                    // 若凭证有效, 检测本地存储 userInfo 是否为空,
          if (Tool.isEmpty(userInfo.value)) {                           // 若 userInfo 为空, 则为初次登录, 尝试自动登录
            // console.log("凭证有效, userInfo 为空")
            autoLogin(response.data.content);
          } else {                                                      // 若 userInfo 不为空, 则正处于登录态
            // console.log("已经是登录态了");
            wsLogin();
            ifLoginIn.value = true;
          }

        }  // 凭证失效, 或者 userInfo 不为空, do nothing
      })
    }

    //-------------WebSocket--------------

    let websocket: any;
    let token: any;

    const onOpen = () => {
      // console.log('WebSocket连接成功，状态码：', websocket.readyState)
    };

    const onMessage = (event: any) => {
      // console.log('WebSocket收到消息：', event.data);
      notification['info']({
        message: '收到消息',
        description: event.data,
      });
    };

    const onError = () => {
      // console.log('WebSocket连接错误，状态码：', websocket.readyState)
    };

    const onClose = () => {
      // console.log('WebSocket连接关闭，状态码：', websocket.readyState)
    };

    const initWebSocket = () => {
      // 连接成功
      websocket.onopen = onOpen;
      // 收到消息的回调
      websocket.onmessage = onMessage;
      // 连接错误
      websocket.onerror = onError;
      // 连接关闭的回调
      websocket.onclose = onClose;
    };

    const wsLogin = () => {
      // WebSocket
      if ('WebSocket' in window) {
        token = Tool.uuid(10);
        // 连接地址：ws://127.0.0.1:8880/ws/xxx
        websocket = new WebSocket(process.env.VUE_APP_WS_SERVER + '/ws/' + token);
        initWebSocket()

        // 关闭
        // websocket.close();
      }
    };

    onMounted(() => {
      checkLoginCert();
    });

    return {
      loginModalVisible,
      loginModalLoading,
      ifLoginIn,

      registerModalVisible,
      registerModalLoading,
      // verifyCode,

      forgetModalVisible,
      forgetModalLoading,
      repeatNewPassword,

      userInModal,
      userInfo,
      verifyBtnDisable,
      userInfoRefresh,

      loginByGitHub,
      loginModalOk,
      loginClick,
      registerClick,
      registerModalOk,
      sendVerifyCode,
      forgetPasswordHandle,
      forgetModalOk,
      dropdownClickHandle,
      userInfoClick,
      getInviteCode,
    };
  }
});
</script>

<style>

@media (max-width: 768px) {
  .header-menu {
    transform: scale(0.8);
    display: inline-block;
    width: 60%;
    max-width: 60%;
    padding-left: 0% !important;
    color: black;
    background-color: white;
  }

  .login-menu {
    transform: scale(0.9);
    color: black;
    float: left;
    width: 40% !important;
    height: 64px;
    /*font-weight: bold;*/
  }

  .login-mark-div {
    position: absolute;
    width: 100%;
    padding-left: 40%;
    max-height: 64px;
  }
}

.header {
  padding: 0 0px !important;
  color: white !important;
  background: white !important;
  /*text-align: center !important;*/

}

/*.header-menu {*/
/*  display: inline-block;*/
/*  width: 85%;*/
/*  padding-left: 20%;*/
/*}*/

.header-menu {
  display: inline-block;
  width: 85%;
  padding-left: 6%; /*与白色页面的边对齐*/
  color: black;
  background-color: white;
}

.login-menu {
  color: black;
  width: 13%;  /*与白色页面的边对齐*/
  float: right;
  /*font-weight: bold;*/
}

.modal-footer-div {
  text-align: center;
}
</style>