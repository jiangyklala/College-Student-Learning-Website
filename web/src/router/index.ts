import {createRouter, createWebHistory, RouteRecordRaw} from 'vue-router'
import Home from "@/views/Home.vue";
import Download from "@/views/download/Download.vue";
import AdminDownload from "@/views/admin/AdminDownload.vue";

const routes: Array<RouteRecordRaw> = [
  {
    path: '/home',
    name: 'home',
    component: Home
  },
  {
    path: '/about',
    name: 'about',
    // 懒加载的写法
    component: () => import(/* webpackChunkName: "about" */ '../views/About.vue')
  },
  {
    path: '/download/Download',
    name: 'download',
    component: Download
  },
  {
    path: '/admin/AdminDownload',
    name: 'AdminDownload',
    component: AdminDownload
  },
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
