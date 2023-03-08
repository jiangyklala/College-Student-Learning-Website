import {createRouter, createWebHistory, RouteRecordRaw} from 'vue-router'
import Home from "@/views/Home.vue";
import Download from "@/views/download/Download.vue";
import AdminDownload from "@/views/admin/AdminDownload.vue";
import AdminCategory from "@/views/admin/AdminCategory.vue";
import VideosPlayer from "@/views/course/VideosPlayer.vue";
import Course from "@/views/course/Course.vue";
import AdminCourse from "@/views/admin/AdminCourse.vue";
import Column from "@/views/column/Column.vue";

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
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
