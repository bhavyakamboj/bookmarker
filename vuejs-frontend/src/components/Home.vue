<template>
  <div class="container">
    <h2>{{msg}}</h2>
    <div v-for="bookmark in bookmarks" :key="bookmark.id">
      <h5><a v-bind:href="bookmark.url" target="_blank">{{bookmark.title}}</a></h5>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Home',
  data () {
    return {
      msg: 'My Bookmarks'
    }
  },
  created () {
    this.$store.dispatch('fetchBookmarks')
  },
  computed: {
    bookmarks () {
      return this.$store.state.bookmarks
    }
  },
  methods: {
  }
}
</script>

<style scoped>

</style>
