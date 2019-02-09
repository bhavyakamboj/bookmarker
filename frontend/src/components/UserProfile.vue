<template>
  <div class="container">
    <div class="row">
      <div class="col-8">
        <form>
          <div class="form-group row">
            <label for="name" class="col-sm-2 col-form-label">Name</label>
            <div class="col-sm-10">
              <input type="text" class="form-control" id="name" v-bind:value="userProfile.name" readonly>
            </div>
          </div>
          <div class="form-group row">
            <label for="email" class="col-sm-2 col-form-label">Email</label>
            <div class="col-sm-10">
              <input type="email" class="form-control" id="email" placeholder="Email" v-bind:value="userProfile.email" readonly>
            </div>
          </div>
        </form>
        <div>
          <h4>
            Bookmarks Created By: {{userProfile.name}}
          </h4>
          <bookmarks-list v-bind:bookmarks="userBookmarks"></bookmarks-list>
        </div>
      </div>

    </div>
  </div>
</template>
<script>
import { mapActions } from 'vuex'
import BookmarksList from './BookmarksList'
export default {
  name: 'UserProfile',
  components: {
    'bookmarks-list': BookmarksList
  },
  data () {
    return {
      userProfile: {},
      userBookmarks: {}
    }
  },
  mounted () {
    this.getUserProfile(this.$route.params.id)
  },
  watch: {
    '$route.params.id': function () {
      this.getUserProfile(this.$route.params.id)
    }
  },
  computed: {
    // ...mapState([ 'user' ])
  },
  methods: {
    ...mapActions([ 'fetchUserProfile', 'fetchBookmarksByUser' ]),
    getUserProfile (userId) {
      this.fetchUserProfile(userId).then(response => {
        this.userProfile = response.data
        this.fetchBookmarksByUser(userId).then(resp => {
          this.userBookmarks = resp.data
        })
      })
    }
  }
}
</script>
