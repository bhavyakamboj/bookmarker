<template>
  <div class="container">
    <div class="row">
      <div class="col-8">
        <h4>
          Bookmarks tagged: [{{selectedTag.name}}]
        </h4>
        <bookmarks-list v-bind:bookmarks="selectedTag.bookmarks"></bookmarks-list>
      </div>
      <div class="col-4">
        <tag-cloud v-bind:tags="tags"></tag-cloud>
      </div>
    </div>
  </div>
</template>
<script>
import { mapActions, mapState } from 'vuex'
import TagCloud from './TagCloud'
import BookmarksList from './BookmarksList'
export default {
  name: 'BookmarksByTag',
  components: {
    'tag-cloud': TagCloud,
    'bookmarks-list': BookmarksList
  },
  data () {
    return {
      msg: 'Bookmarks'
    }
  },
  mounted () {
    this.fetchTags()
    this.fetchBookmarksByTag(this.$route.params.tag)
  },
  watch: {
    '$route.params.tag': function () {
      this.fetchBookmarksByTag(this.$route.params.tag)
    }
  },
  computed: {
    ...mapState([ 'selectedTag', 'tags' ])
  },
  methods: {
    ...mapActions([ 'fetchBookmarksByTag', 'fetchTags' ])
  }
}
</script>

<style scoped>

</style>
