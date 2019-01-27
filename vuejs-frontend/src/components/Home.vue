<template>
  <div class="container">
    <div class="row">
      <div class="col-12">
        <div v-for="bookmark in bookmarks" :key="bookmark.id">
          <h5><a v-bind:href="bookmark.url" target="_blank">{{bookmark.title}}</a></h5>
        </div>
      </div>
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
  mounted () {
    this.loadBookmarks()
  },
  computed: {
    bookmarks () {
      return this.$store.state.bookmarks
    }
  },
  methods: {
    loadBookmarks () {
      this.$store.dispatch('fetchBookmarks')
    }
  }
}
</script>

<style scoped>

</style>
