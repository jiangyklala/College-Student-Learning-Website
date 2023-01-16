<template>
  <a-layout-header>
    <div class="logo" />
    <a-menu
        theme="dark"
        mode="horizontal"
        :style="{ lineHeight: '64px' }"
    >
      <a-menu-item key="1">
        <router-link to="/Home">首页</router-link>
      </a-menu-item>
      <a-menu-item key="2">
        <router-link to="/download/Download">下载</router-link>
      </a-menu-item>
      <a-menu-item key="3">
        <router-link to="/admin/AdminDownload">电子书管理</router-link>
      </a-menu-item>
      <a-menu-item key="4">
        <router-link to="/admin/AdminCategory">分类管理</router-link>
      </a-menu-item>
      <a-menu-item key="5">
        <router-link to="/About">关于</router-link>
      </a-menu-item>
    </a-menu>
  </a-layout-header>
</template>

<script lang="ts">
import {defineComponent} from 'vue';

export default defineComponent({
  name: 'the-header',
  // props: {
  //   msg: String,
  // } // 父子组件间传递数据
});
</script>