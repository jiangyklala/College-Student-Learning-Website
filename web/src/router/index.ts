import {createRouter, createWebHistory, RouteRecordRaw} from 'vue-router'
import Home from "@/views/Home.vue";
import Download from "@/views/download/Download.vue";
import AdminDownload from "@/views/admin/AdminDownload.vue";
import AdminCategory from "@/views/admin/AdminCategory.vue";
import VideosPlayer from "@/views/course/VideosPlayer.vue";
import Course from "@/views/course/Course.vue";
import AdminCourse from "@/views/admin/AdminCourse.vue";
import AdminColumn from "@/views/admin/AdminColumn.vue";
import AdminDoc from "@/views/admin/AdminDoc.vue";
import Column from "@/views/column/Column.vue";
import Doc from "@/views/column/Doc.vue";
import Chatgpt from "@/views/chatgpt/Chatgpt.vue";
import Image from "@/views/chatgpt/Image.vue";
import Pay from "@/views/chatgpt/Pay.vue";

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
        path: '/course/VideosPlayer',
        name: 'VideosPlayer',
        component: VideosPlayer
    },
    {
        path: '/admin/AdminDownload',
        name: 'AdminDownload',
        component: AdminDownload
    },
    {
        path: '/admin/AdminCategory',
        name: 'AdminCategory',
        component: AdminCategory
    },
    {
        path: '/course/Course',
        name: 'Course',
        component: Course
    },
    {
        path: '/admin/AdminCourse',
        name: 'AdminCourse',
        component: AdminCourse
    },
    {
        path: '/column/Column',
        name: 'Column',
        component: Column
    },
    {
        path: '/admin/AdminColumn',
        name: 'AdminColumn',
        component: AdminColumn
    },
    {
        path: '/admin/AdminDoc',
        name: 'AdminDoc',
        component: AdminDoc
    },
    {
        path: '/column/Doc',
        name: 'Doc',
        component: Doc
    },
    {
        path: '/chatgpt/Chatgpt',
        name: 'Chatgpt',
        component: Chatgpt
    },
    {
        path: '/chatgpt/Image',
        name: 'Image',
        component: Image
    },
    {
        path: '/chatgpt/Pay',
        name: 'Pay',
        component: Pay
    },
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
