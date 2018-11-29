import Vue from 'vue'
import Vuex from 'vuex'
import { HTTP } from './http-utils'

Vue.use(Vuex)

// initialize state
const state = {
  user: {},
  auth_token: {},
  bookmarks: []
}

// initialize mutations
const mutations = {
  createBookmark (state, bookmark) {
    state.bookmarks.push(bookmark)
  },
  loadBookmarks (state, bookmarks) {
    state.bookmarks = bookmarks
  },
  setAuth (state, authResponse) {
    state.auth_token = authResponse
    localStorage.setItem('access_token', authResponse.access_token)
  },
  clearAuth (state) {
    state.auth_token = {}
    localStorage.removeItem('access_token')
  }
}

const actions = {
  fetchBookmarks ({ commit }) {
    HTTP.get('bookmarks')
      .then((response) => {
        commit('loadBookmarks', response.data)
      })
      .catch((err) => {
        console.log(err)
      })
  },

  createBookmark ({ commit, state }, bookmark) {
    return new Promise((resolve, reject) => {
      console.log(bookmark)
      HTTP.post('bookmarks', bookmark)
        .then(response => {
          // commit('setAuth', response.data)
          resolve(response)
        }, error => {
          reject(error)
        })
    })
  },

  login ({ commit, state }, credentials) {
    return new Promise((resolve, reject) => {
      HTTP.post('auth/login', credentials, {
        headers: {
          'Content-Type': 'application/json'
        }
      })
        .then(response => {
          commit('setAuth', response.data)
          resolve(response)
        }, error => {
          reject(error)
        })
    })
  },
  logout ({ commit }) {
    commit('clearAuth')
  }
}

export default new Vuex.Store({
  state: state,
  mutations: mutations,
  actions: actions
})
