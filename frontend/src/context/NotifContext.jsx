import { createContext, useContext, useState, useCallback, useEffect } from 'react'
import { getUnreadCount, getNotifications } from '../api/notifications'
import { useAuth } from './AuthContext'

const NotifContext = createContext(null)

export function NotifProvider({ children }) {
  const { isLoggedIn } = useAuth()
  const [unreadCount, setUnreadCount] = useState(0)

  const refreshUnreadCount = useCallback(async () => {
    if (!isLoggedIn) return
    try {
      const { data } = await getUnreadCount()
      setUnreadCount(data.count)
    } catch (error) {
      console.error('Failed to fetch unread count', error)
    }
  }, [isLoggedIn])

  useEffect(() => {
    refreshUnreadCount()
    const interval = setInterval(refreshUnreadCount, 30000) // Poll every 30s
    return () => clearInterval(interval)
  }, [refreshUnreadCount])

  return (
    <NotifContext.Provider value={{ unreadCount, refreshUnreadCount }}>
      {children}
    </NotifContext.Provider>
  )
}

export const useNotif = () => useContext(NotifContext)
