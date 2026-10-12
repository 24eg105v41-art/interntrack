import { Link, NavLink, useNavigate } from 'react-router-dom'
import { clearAuth, getRole, getUser, isLoggedIn } from '../api.js'

export default function Navbar() {
  const navigate = useNavigate()
  const loggedIn = isLoggedIn()
  const role = getRole()
  const user = getUser()

  const logout = () => {
    clearAuth()
    navigate('/login')
  }

  return (
    <header className="navbar">
      <div className="navbar-inner">
        <Link to="/" className="brand">
          <span className="brand-mark">IT</span>
          <span className="brand-text">Intern<strong>Track</strong></span>
        </Link>

        <nav className="nav-links">
          <NavLink to="/" end>
            Home
          </NavLink>
          {loggedIn && role === 'STUDENT' && (
            <>
              <NavLink to="/dashboard">Dashboard</NavLink>
              <NavLink to="/internships">Internships</NavLink>
              <NavLink to="/offers">Offers</NavLink>
              <NavLink to="/deadlines">Deadlines</NavLink>
            </>
          )}
          {loggedIn && role === 'ADMIN' && <NavLink to="/admin">Admin</NavLink>}
        </nav>

        <div className="nav-actions">
          {loggedIn ? (
            <>
              <span className="nav-user" title={user?.email || ''}>
                {user?.name || 'Account'}
              </span>
              <button className="btn btn-ghost" onClick={logout}>
                Logout
              </button>
            </>
          ) : (
            <>
              <Link className="btn btn-ghost" to="/login">
                Login
              </Link>
              <Link className="btn btn-primary" to="/register">
                Get started
              </Link>
            </>
          )}
        </div>
      </div>
    </header>
  )
}
