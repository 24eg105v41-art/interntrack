import { Navigate, Route, Routes, useLocation } from 'react-router-dom'
import Navbar from './components/Navbar.jsx'
import Home from './pages/Home.jsx'
import Login from './pages/Login.jsx'
import Register from './pages/Register.jsx'
import Dashboard from './pages/Dashboard.jsx'
import Internships from './pages/Internships.jsx'
import Offers from './pages/Offers.jsx'
import Deadlines from './pages/Deadlines.jsx'
import Admin from './pages/Admin.jsx'
import { isLoggedIn, getRole } from './api.js'

function RequireAuth({ children }) {
  const location = useLocation()
  if (!isLoggedIn()) {
    return <Navigate to="/login" state={{ from: location.pathname }} replace />
  }
  return children
}

function RequireAdmin({ children }) {
  if (!isLoggedIn()) return <Navigate to="/login" replace />
  if (getRole() !== 'ADMIN') return <Navigate to="/dashboard" replace />
  return children
}

export default function App() {
  return (
    <div className="app">
      <Navbar />
      <main className="container">
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route
            path="/dashboard"
            element={
              <RequireAuth>
                <Dashboard />
              </RequireAuth>
            }
          />
          <Route
            path="/internships"
            element={
              <RequireAuth>
                <Internships />
              </RequireAuth>
            }
          />
          <Route
            path="/offers"
            element={
              <RequireAuth>
                <Offers />
              </RequireAuth>
            }
          />
          <Route
            path="/deadlines"
            element={
              <RequireAuth>
                <Deadlines />
              </RequireAuth>
            }
          />
          <Route
            path="/admin"
            element={
              <RequireAdmin>
                <Admin />
              </RequireAdmin>
            }
          />
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </main>
      <footer className="footer">
        InternTrack · Internship Application, Offer &amp; Deadline Management System
      </footer>
    </div>
  )
}
