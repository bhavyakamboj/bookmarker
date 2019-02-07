import Vue from 'vue'
import Vuex from 'vuex'
import { HTTP } from './http-utils'

Vue.use(Vuex)

const state = {
  user: {},
  auth_token: {},
  bookmarks: [],
  selectedTag: {},
  tags: []
}

const mutations = {
  setTags (state, tags) {
    state.tags = tags
  },
  setBookmarks (state, bookmarks) {
    state.bookmarks = bookmarks
  },
  setSelectedTag (state, selectedTag) {
    state.selectedTag = selectedTag
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
  async fetchTags ({ commit }) {
    let tags = (await HTTP.get('tags')).data
    commit('setTags', tags)
  },

  async fetchBookmarks ({ commit }) {
    let bookmarks = (await HTTP.get('bookmarks')).data
    commit('setBookmarks', bookmarks)
  },

  async fetchBookmarksByTag ({ commit }, tag) {
    let selectedTag = (await HTTP.get(`bookmarks/tagged/${tag}`)).data
    commit('setSelectedTag', selectedTag)
  },

  async createBookmark ({ commit, state }, bookmark) {
    return HTTP.post('bookmarks', bookmark)
  },

  async login ({ commit, state }, credentials) {
    let response = await HTTP.post('auth/login', credentials)
    if (response.status === 200) {
      commit('setAuth', response.data)
    }
  },

  async register ({ commit, state }, user) {
    return HTTP.post('users', user)
  },

  logout ({ commit }) {
    commit('clearAuth')
  }

}

const getters = {
}

export default new Vuex.Store({
  state: state,
  mutations: mutations,
  actions: actions,
  getters: getters
})
