<template>
  <a-layout-content class="layout-content">
    <a-card class="show-card" :body-style="{ display: 'flex', flexDirection: 'column' }">
      <div>
        <div >
          <p style="float: left; color: rgb(156, 160, 164); ">当前用户类型: &nbsp;&nbsp;</p>
        </div>
        <div >
          <p style="float: left; color: rgb(151, 188, 255); font-size: 30px;">{{userTypeName}}&nbsp;&nbsp;&nbsp;&nbsp;</p>
        </div>
        <div v-if="userInfo.type === 2">
          <p style="float: left; color: rgb(156, 160, 164); ">剩余时间: &nbsp;&nbsp;</p>
        </div>
        <div v-if="userInfo.type === 2">
          <p style="float: left; color: rgb(151, 188, 255); font-size: 30px;">{{userRemainDays}} 天</p>
        </div>
      </div>

      <p style="float: left; color: rgb(156, 160, 164)">选择需要充值的类型: </p>


      <div class="show-div" >
        <a-radio-group v-model:value="chooseValue">
          <a-space style="width: 100%" size="large">
            <a-radio-button class="show-radio-button" value="1">月会员 ￥35</a-radio-button>
            <a-radio-button class="show-radio-button" value="2">季度会员 ￥70</a-radio-button>
            <a-radio-button class="show-radio-button" value="3">年度会员 ￥200</a-radio-button>
          </a-space>
        </a-radio-group>
      </div>


      <div style="text-align: center;">
        <br>
        <br>
        <br>
        <a-button class="pay-OK-button" type="primary" size="middle" @click="payConfirmBtnClick" style="">充值</a-button>
      </div>

    </a-card>

  </a-layout-content>

  <a-modal v-model:visible="confirmModalVisible"
           title="请确认您的支付订单"
           @ok="payWithBtnClick"
           cancelText="再想想"
           ok-text="是的"
           style="width: 40%; height: 40%;">

    <p style="float: left; color: rgb(156, 160, 164)">您要充值的是: </p>
    <br>
    <br>
    <p style="float: left; color: rgb(151, 188, 255); font-size: 25px;">{{userTypeName}}</p>
    <br>
    <br>
    <br>
    <p style="float: left; color: rgb(156, 160, 164)">需要支付: </p>
    <br>
    <br>
    <p style="float: left; color: rgb(151, 188, 255); font-size: 25px;">{{userPayCount}} 元</p>
    <br>
    <br>
  </a-modal>


</template>

<script lang="ts">
import {computed, defineComponent, onMounted, ref, watch} from "vue";
import {message} from "ant-design-vue";
import store from "@/store";
import {useRouter} from "vue-router";
import {Tool} from "@/utils/tool";

export default defineComponent ({
  name: "Pay",
  setup () {

    const userInfo = computed(() => {
      return store.state.userInfo;
    });
    const chooseValue = ref(0);
    const confirmModalVisible = ref(false);
    const userTypeName = ref("未知");



    const payConfirmBtnClick = () => {
      if (chooseValue.value === 0) {
        message.warn("请选择需要充值的类型");
        return;
      }

      if (userInfo.value.type === 2 || userInfo.value.type === 3) {
        message.warn("暂不支持续费呦");
        return;
      }


      confirmModalVisible.value = true;
      userPayCount.value = getUserPayCount(chooseValue.value);
      // console.log(chooseValue.value);
    }

    const payWithBtnClick = () => {
      confirmModalVisible.value = false;

      goToPayingPage(chooseValue.value);

    }

    const getUserTypeName = (type : number) => {
      let userTypeNameRes = "未知";
      let typeStr = type.toString();
      switch (typeStr) {
        case "1":
          userTypeNameRes = "普通用户";
          break;
        case "2":
          userTypeNameRes =  "会员";
          break;
        case "3":
          userTypeNameRes =  "超级会员";
          break;
      }

      return userTypeNameRes;
    }

    const getUserPayCount = (type : number) => {
      let userPayCountRes = 0;
      let typeStr = type.toString();
      switch (typeStr) {
        case "1":
          userPayCountRes = 35;
          break;
        case "2":
          userPayCountRes = 70;
          break;
        case "3":
          userPayCountRes = 200;
          break;
      }

      return userPayCountRes;
    }

    // watch(chooseValue, (newValue: number, oldValue: number) => {
    //   // console.log(`count 变化了：${oldValue} => ${newValue}`);
    //   console.log(newValue);
    //   let newValueStr = newValue.toString();
    //   switch (newValueStr) {
    //     case "1":
    //       userTypeName.value = "普通用户";
    //       console.log("11");
    //       break;
    //     case "2":
    //       userTypeName.value =  "会员";
    //       console.log("11");
    //       break;
    //     case "3":
    //       console.log("11");
    //       userTypeName.value =  "超级会员";
    //       break;
    //   }
    // });



    const router = useRouter();

    const userRemainDays = ref("null");

    const userPayCount = ref(0);

    const goToPayingPage = (chooseValue : any) => {
      // console.log(chooseValue);
      router.push({
        name: 'Paying',
        params: {
          chooseValue: chooseValue
        }
      });
    };


    onMounted(() => {
      if (Tool.isEmpty(userInfo.value)) {
        watch(userInfo, (newValue: any, oldValue: any) => {
          userTypeName.value = getUserTypeName(newValue.type);
          userRemainDays.value = newValue.remainDays;
        });
      } else {
        userTypeName.value = getUserTypeName(userInfo.value.type);
        userRemainDays.value = userInfo.value.remainDays;
      }


    })

    return {
      chooseValue,
      userInfo,
      confirmModalVisible,
      userTypeName,
      userRemainDays,
      userPayCount,

      getUserTypeName,
      payWithBtnClick,
      getUserPayCount,
      payConfirmBtnClick,
    };
  }
})
</script>

<style scoped>
@media (max-width: 768px) {
  .show-card {
    font-size: 5px;
  }

  .show-div {
    font-size: 1px;
  }

  .show-radio-button {
    font-size: 1px;
    height: 100%;
  }

  .pay-OK-button {
    width: 30% !important;
  }
}

.layout-content {
  padding-top: 60px;
  width: 80%;
  height: 100%;
  min-height: 200px;
  margin: 20px auto 100px;
  overflow: hidden;
  background: rgb(237, 239, 242);
}

.show-card {
  width: 100%;
}

.show-div {
  text-align: center
}

.pay-OK-button {
  width: 20%;
  text-align: center;
}
</style>