import Vue from 'vue'
import Router from 'vue-router'
import Home from '@/components/Home'
import Login from '@/components/Login'
import NewBookmark from '@/components/NewBookmark'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: Login
    },
    {
      path: '/bookmarks',
      name: 'Home',
      component: Home
    },
    {
      path: '/new-bookmark',
      name: 'NewBookmark',
      component: NewBookmark
    },
    {
      path: '*',
      redirect: '/bookmarks'
    }
  ]
})
