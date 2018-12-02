<template>
  <div class="container">
    <div class="row">
      <div class="col-3">
        <div class="list-group list-group-flush">
          <router-link class="list-group-item" to="/bookmarks/all">
            <i class="fa fa-home" aria-hidden="true"></i> Home
          </router-link>
          <router-link class="list-group-item" to="/bookmarks/liked">
            <i class="fa fa-heart-o" aria-hidden="true"></i> Liked
          </router-link>
          <router-link class="list-group-item" to="/bookmarks/archived">
            <i class="fa fa-archive" aria-hidden="true"></i> Archived
          </router-link>
          <router-link class="list-group-item" to="/videos">
            <i class="fa fa-video-camera" aria-hidden="true"></i> Videos
          </router-link>
          <router-link class="list-group-item" to="/notes">
            <i class="fa fa-sticky-note-o" aria-hidden="true"></i> Notes
          </router-link>

        </div>
      </div>
      <div class="col-9">
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
  watch: {
    '$route.params.filter': function (filter) {
      this.loadBookmarks()
    }
  },
  created () {
    this.loadBookmarks()
  },
  computed: {
    bookmarks () {
      return this.$store.state.bookmarks
    }
  },
  methods: {
    loadBookmarks () {
      this.$store.dispatch('fetchBookmarks', this.$route.params.filter)
    }
  }
}
</script>

<style scoped>

</style>
