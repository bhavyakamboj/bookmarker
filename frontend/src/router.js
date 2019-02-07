import Vue from 'vue'
import Router from 'vue-router'
import Home from './components/Home'
import Bookmarks from './components/Bookmarks'
import BookmarksByTag from './components/BookmarksByTag'
import Login from './components/Login'
import Registration from './components/Registration'
import NewBookmark from './components/NewBookmark'

Vue.use(Router)

const router = new Router({
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: Login,
      meta: { requiresAuth: false }
    },
    {
      path: '/registration',
      name: 'Registration',
      component: Registration,
      meta: { requiresAuth: false }
    },
    {
      path: '/bookmarks',
      component: Home,
      children: [
        {
          path: '',
          name: 'Bookmarks',
          component: Bookmarks,
          meta: { requiresAuth: false }
        },
        {
          path: 'tagged/:tag',
          name: 'BookmarksByTag',
          component: BookmarksByTag,
          meta: { requiresAuth: false }
        }
      ]
    },
    {
      path: '/new-bookmark',
      name: 'NewBookmark',
      component: NewBookmark,
      meta: { requiresAuth: true }
    },
    {
      path: '*',
      redirect: '/bookmarks'
    }
  ]
})

router.beforeEach((to, from, next) => {
  // redirect to login page if not logged in and trying to access a restricted page
  // const publicPages = ['/login', '/registration', '/bookmarks']
  // const authRequired = !publicPages.includes(to.path)

  if (to.matched.some(record => record.meta.requiresAuth)) {
    const accessToken = localStorage.getItem('access_token')
    if (!accessToken) {
      return next('/login')
    }
  }
  next()
})

export default router
