<template>
  <div class="row justify-content-center">
    <div class="col-md-6">
      <h2>Create New Bookmark</h2>
      <form @submit.prevent="addNewBookmark">
        <div v-if="error" class="alert alert-danger">
          {{ error }}
        </div>
        <div class="form-group">
          <label>Title</label>
          <input type="text" class="form-control" v-model="newBookmark.title" placeholder="Title">
        </div>
        <div class="form-group">
          <label>URL</label>
          <input type="text" class="form-control" v-model="newBookmark.url" placeholder="URL">
        </div>

        <button type="submit" class="btn btn-primary">Submit</button>
      </form>
    </div>
  </div>
</template>

<script>
export default {
  name: 'NewBookmark',
  data () {
    return {
      newBookmark: {},
      error: ''
    }
  },
  methods: {
    addNewBookmark () {
      this.$store.dispatch('createBookmark', this.newBookmark).then(response => {
        console.log('saved link successfully')
        this.$router.push('/')
      }, error => {
        console.error('Save failed', error)
        this.error = 'There is some error in saving Bookmark'
      })
    }
  }
}
</script>
