import Vue from 'vue'
import Router from 'vue-router'
import Index from '@/pages/Index'
import Login from '@/pages/Login'
import Register from '@/pages/Register'
import ImageDetail from'@/pages/ImageDetail'
import SearchInit from '@/pages/SearchInit'
import Search from '@/pages/Search'
import Upload from '@/pages/Upload'
import Home from '@/pages/Home'
import Message from '@/pages/Message'
import Favor from '@/pages/Favor'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Index',
      component: Index
    },
    {
      path: '/login',
      name: 'Login',
      component: Login
    },
    {
      path: '/register',
      name: 'Register',
      component: Register
    },
    {
      path: '/imageDetail/:imageId',
      name: 'ImageDetail',
      component: ImageDetail
    },
    {
      path: '/search',
      name: 'SearchInit',
      component: SearchInit
    },
    {
      path: '/search/:input/:scope/:rule',
      name: 'Search',
      component: Search
    },
    {
      path: '/upload',
      name: 'Upload',
      component: Upload,
      meta:{
        requireAuth: true
      }
    },
    {
      path: '/home',
      name: 'Home',
      component: Home,
      meta:{
        requireAuth: true
      }
    },
    {
      path: '/message',
      name: 'Message',
      component: Message,
      meta: {
        requireAuth: true
      }
    },
    {
      path: '/favor/:username',
      name: 'Favor',
      component: Favor,
    },
  ]
})
