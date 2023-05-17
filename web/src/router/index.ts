import {createRouter, createWebHistory, RouteRecordRaw} from 'vue-router'
import Home from "@/views/Home.vue";
import Chatgpt from "@/views/chatgpt/Chatgpt.vue";
import Image from "@/views/chatgpt/Image.vue";

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
        component: () => import('../views/download/Download.vue')
    },
    {
        path: '/course/VideosPlayer',
        name: 'VideosPlayer',
        component: () => import('../views/course/VideosPlayer.vue')

    },
    {
        path: '/admin/AdminDownload',
        name: 'AdminDownload',
        component: () => import('../views/admin/AdminDownload.vue')

    },
    {
        path: '/admin/AdminCategory',
        name: 'AdminCategory',
        component: () => import('../views/admin/AdminCategory.vue')

    },
    {
        path: '/course/Course',
        name: 'Course',
        component: () => import('../views/course/Course.vue')

    },
    {
        path: '/admin/AdminCourse',
        name: 'AdminCourse',
        component: () => import('../views/admin/AdminCourse.vue')

    },
    {
        path: '/column/Column',
        name: 'Column',
        component: () => import('../views/column/Column.vue')

    },
    {
        path: '/admin/AdminColumn',
        name: 'AdminColumn',
        component: () => import('../views/admin/AdminColumn.vue')

    },
    {
        path: '/admin/AdminDoc',
        name: 'AdminDoc',
        component: () => import('../views/admin/AdminDoc.vue')

    },
    {
        path: '/column/Doc',
        name: 'Doc',
        component: () => import('../views/column/Doc.vue')

    },
    {
        path: '/chatgpt/Chatgpt',
        name: 'Chatgpt',
        component: Chatgpt
    },
    {
        path: '/',
        name: '/',
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
        component: () => import('../views/chatgpt/Pay.vue')
    },
    {
        path: '/chatgpt/Invite',
        name: 'Invite',
        component: () => import('../views/chatgpt/Invite.vue')
    },
    {
        path: '/chatgpt/Paying/:chooseValue',
        name: 'Paying',
        component: () => import('../views/chatgpt/Paying.vue')
    },
]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
})

export default router
