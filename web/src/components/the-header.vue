<template>
  <a-layout-header class="header">
    <a-menu
        theme="light"
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
      <a-menu-item key="7">
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

<style>

.header {
  color: white !important;
  background: white !important;
  text-align: center !important;
}

</style>