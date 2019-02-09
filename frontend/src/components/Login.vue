<template>
  <div class="row justify-content-center">
    <div class="col-md-4">
      <div class="card">
        <div class="card-header text-center">
          <h3>Login Form</h3>
        </div>
        <div class="card-body">
          <form @submit.prevent="doLogin">
            <div v-if="error" class="alert alert-danger">
              {{ error }}
            </div>
            <div class="form-group">
              <label for="email"><strong>Email<span style="color: red">*</span></strong></label>
              <input type="email" class="form-control" id="email" placeholder="Enter email" v-model="credentials.username">
            </div>
            <div class="form-group">
              <label for="password"><strong>Password<span style="color: red">*</span></strong></label>
              <input type="password" class="form-control" id="password" placeholder="Password" v-model="credentials.password">
            </div>

            <button type="submit" class="btn btn-primary">Login</button>
          </form>
        </div>
      </div>
    </div>
  </div>

</template>

<script>
import { mapActions } from 'vuex'
export default {
  name: 'Login',
  data () {
    return {
      credentials: {},
      error: ''
    }
  },
  methods: {
    ...mapActions([ 'login', 'fetchCurrentUser' ]),
    doLogin () {
      this.login(this.credentials).then(response => {
        console.log('Login successful')
        this.fetchCurrentUser().then(resp => {
          window.eventBus.$emit('loggedin')
          this.$router.push('/bookmarks')
        })
      }, error => {
        console.error('Login failed', error)
        this.error = 'Login failed'
      })
    }
  }
}
</script>
