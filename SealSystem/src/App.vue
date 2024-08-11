<template>
  <div id="app">
    <router-view/>
  </div>
</template>
<script>


  export default {
    mounted() {
      window.addEventListener('onbeforeunload', () => {
        var storage = window.localStorage;
        storage.clear()
        store.clearUser()
        this.$router.push({
          path: '/'
        })
      });
    }
  }
</script>
