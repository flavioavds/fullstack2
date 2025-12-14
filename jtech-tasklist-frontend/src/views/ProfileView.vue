<template>
  <div class="profile">
    <h2>Meu Perfil</h2>

    <form @submit.prevent="handleUpdate">
      <div class="field">
        <label>Nome</label>
        <input v-model="name" placeholder="Nome completo" required />
      </div>

      <div class="field">
        <label>Email</label>
        <input v-model="email" type="email" placeholder="Email" required />
      </div>

      <!-- Botão para mostrar/ocultar o campo de senha -->
      <div class="field">
        <button type="button" class="toggle-password" @click="showPassword = !showPassword">
          {{ showPassword ? 'Cancelar alteração de senha' : 'Alterar senha' }}
        </button>
      </div>

      <!-- Campo de senha apenas se showPassword for true -->
      <div class="field" v-if="showPassword">
        <label>Nova Senha</label>
        <div class="password-wrapper">
          <input
            v-model="password"
            :type="showPasswordField ? 'text' : 'password'"
            placeholder="Digite a nova senha"
          />
          <button type="button" class="eye-btn" @click="showPasswordField = !showPasswordField">
            <svg v-if="showPasswordField" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                d="M13.875 18.825A10.05 10.05 0 0112 19c-5.523 0-10-4.477-10-10S6.477-1 12-1s10 4.477 10 10c0 1.05-.172 2.062-.494 3.016M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
            </svg>
            <svg v-else xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                d="M13.875 18.825A10.05 10.05 0 0112 19c-5.523 0-10-4.477-10-10S6.477-1 12-1s10 4.477 10 10c0 1.05-.172 2.062-.494 3.016M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
              <line x1="3" y1="3" x2="21" y2="21" stroke="currentColor" stroke-width="2" stroke-linecap="round" />
            </svg>
          </button>
        </div>
        <small>Ao realizar alteração de senha será direcionado para página de login em 5 segundos!</small>
      </div>

      <button type="submit" :disabled="loading">
        {{ loading ? 'Atualizando...' : 'Atualizar' }}
      </button>

      <button type="button" class="back-btn" @click="goBack">
        ← Voltar ao Dashboard
      </button>

      <p v-if="error" class="error">{{ error }}</p>
      <p v-if="success" class="success">{{ success }}</p>

      <p v-if="needsLogin" class="info">
        Você alterou seu email ou senha. Será necessário fazer login novamente.
        Redirecionando em {{ countdown }} segundos...
      </p>
    </form>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import http from '@/api/http'
import { useAuthStore } from '@/stores/auth.store'
import { AxiosError } from 'axios'

const router = useRouter()
const authStore = useAuthStore()

const name = ref('')
const email = ref('')
const password = ref('')
const loading = ref(false)
const error = ref<string | null>(null)
const success = ref<string | null>(null)
const showPassword = ref(false)
const showPasswordField = ref(false)
const needsLogin = ref(false)
const countdown = ref(5)

onMounted(async () => {
  if (!authStore.user) await authStore.fetchMe()
  name.value = authStore.user?.name || ''
  email.value = authStore.user?.email || ''
})

async function handleUpdate() {
  loading.value = true
  error.value = null
  success.value = null
  needsLogin.value = false
  countdown.value = 5

  try {
    const payload: Record<string, string> = { name: name.value, email: email.value }

    const emailChanged = email.value !== authStore.user?.email
    const passwordChanged = showPassword.value && password.value

    if (passwordChanged) payload.password = password.value

    const { data } = await http.put('/user/me', payload, {
      headers: { Authorization: `Bearer ${authStore.token}` }
    })

    authStore.user = data
    success.value = 'Dados atualizados com sucesso!'

    if (emailChanged || passwordChanged) {
      needsLogin.value = true
      const interval = setInterval(() => {
        countdown.value -= 1
        if (countdown.value <= 0) {
          clearInterval(interval)
          authStore.clear()
          router.push({ name: 'login' })
        }
      }, 1000)
    }

    password.value = ''
    showPassword.value = false
    showPasswordField.value = false
  } catch (err: unknown) {
    if (err instanceof AxiosError) {
      error.value = err.response?.data?.message || 'Erro ao atualizar'
      if (err.response?.status === 401) {
        alert('Sessão expirada. Você será redirecionado para login.')
        authStore.clear()
        router.push({ name: 'login' })
      }
    } else if (err instanceof Error) error.value = err.message
    else error.value = 'Erro desconhecido'
  } finally {
    loading.value = false
  }
}

function goBack() {
  router.push({ name: 'dashboard' })
}
</script>

<style scoped>
.profile {
  max-width: 500px;
  margin: 40px auto;
  padding: 20px;
  background: #f9fafb;
  border-radius: 12px;
  box-shadow: 0 5px 15px rgba(0,0,0,0.05);
}

.field {
  display: flex;
  flex-direction: column;
  gap: 8px;
  margin-bottom: 16px;
}

input {
  width: 100%;
  padding: 10px 12px;
  font-size: 15px;
  border-radius: 8px;
  border: 1px solid #d1d5db;
  box-sizing: border-box;
}

/* SENHA */
.password-wrapper {
  position: relative;
}

.password-wrapper input {
  padding-right: 36px;
}

.eye-btn {
  position: absolute;
  right: 10px;
  top: 50%;
  transform: translateY(-50%);
  background: none;
  border: none;
  padding: 0;
  cursor: pointer;
  color: #6b7280;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 24px;
  height: 24px;
}

.eye-btn:hover {
  color: #2563eb;
}

.eye-btn svg {
  width: 20px;
  height: 20px;
}

button {
  width: 100%;
  padding: 12px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  background: #2563eb;
  color: white;
  margin-bottom: 8px;
}

button.back-btn {
  background: #6b7280;
}

button.toggle-password {
  background: #fbbf24;
  color: #000;
  width: auto;
  padding: 8px 12px;
  align-self: flex-start;
  margin-bottom: 12px;
}

button:hover {
  opacity: 0.9;
}

.error {
  color: red;
  margin-top: 10px;
  text-align: center;
}

.success {
  color: green;
  margin-top: 10px;
  text-align: center;
}

.info {
  color: #b45309;
  margin-top: 10px;
  text-align: center;
  font-weight: 600;
}
</style>
