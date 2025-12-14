<template>
  <div class="login-page">
    <!-- Botão Home no topo direito -->
    <button class="home-btn" @click="goHome">Home</button>

    <div class="login-card">
      <h1>TaskList</h1>
      <p class="subtitle">Acesse sua conta</p>

      <form @submit.prevent="handleLogin">
        <div class="field">
          <label>Email</label>
          <input
            v-model="email"
            type="email"
            placeholder="seu@email.com"
            required
          />
        </div>

        <div class="field">
          <label>Senha</label>
          <input
            v-model="password"
            type="password"
            placeholder="••••••••"
            required
          />
        </div>

        <button type="submit" :disabled="loading">
          {{ loading ? 'Entrando...' : 'Entrar' }}
        </button>

        <p v-if="error" class="error">
          {{ error }}
        </p>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useAuthStore } from '@/stores/auth.store'
import { useRouter } from 'vue-router'

const email = ref('')
const password = ref('')
const loading = ref(false)
const error = ref<string | null>(null)

const authStore = useAuthStore()
const router = useRouter()

async function handleLogin() {
  loading.value = true
  error.value = null

  try {
    await authStore.login(email.value, password.value)
    router.push({ name: 'dashboard' })
  } catch {
    error.value = 'Usuário ou senha inválidos'
  } finally {
    loading.value = false
  }
}

// Função para voltar para home
function goHome() {
  router.push('/')
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #eef2f7, #d9e2ec);
  position: relative; /* necessário para posicionar o botão Home */
}

/* Botão Home fixo no topo direito */
.home-btn {
  position: absolute;
  top: 20px;
  right: 20px;
  background: #2563eb;
  color: white;
  border: none;
  border-radius: 8px;
  padding: 8px 16px;
  font-weight: 600;
  cursor: pointer;
}

.home-btn:hover {
  opacity: 0.9;
}

.login-card {
  background: #ffffff;
  width: 100%;
  max-width: 420px;
  padding: 40px;
  border-radius: 12px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.08);
}

h1 {
  text-align: center;
  margin-bottom: 4px;
}

.subtitle {
  text-align: center;
  color: #6b7280;
  margin-bottom: 32px;
}

.field {
  margin-bottom: 20px;
}

label {
  display: block;
  margin-bottom: 6px;
  font-weight: 500;
  color: #374151;
}

input {
  width: 100%;
  height: 48px;
  padding: 0 14px;
  font-size: 16px;
  border: 1px solid #d1d5db;
  border-radius: 8px;
  transition: border-color 0.2s, box-shadow 0.2s;
}

input:focus {
  outline: none;
  border-color: #2563eb;
  box-shadow: 0 0 0 3px rgba(37, 99, 235, 0.15);
}

button[type="submit"] {
  width: 100%;
  height: 48px;
  margin-top: 10px;
  background: #2563eb;
  color: #ffffff;
  font-size: 16px;
  font-weight: 600;
  border: none;
  border-radius: 8px;
  cursor: pointer;
}

button:disabled {
  background: #93c5fd;
  cursor: not-allowed;
}

.error {
  margin-top: 16px;
  text-align: center;
  color: #dc2626;
  font-size: 14px;
}
</style>
