import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import api from '../api.js'

const initial = {
  name: '',
  email: '',
  password: '',
  college: '',
  branch: '',
  graduationYear: '',
  cgpa: '',
  skills: ''
}

export default function Register() {
  const navigate = useNavigate()
  const [form, setForm] = useState(initial)
  const [error, setError] = useState('')
  const [busy, setBusy] = useState(false)

  const change = (e) => setForm({ ...form, [e.target.name]: e.target.value })

  const submit = async (e) => {
    e.preventDefault()
    setError('')
    setBusy(true)
    try {
      const payload = {
        ...form,
        graduationYear: form.graduationYear ? Number(form.graduationYear) : null,
        cgpa: form.cgpa ? Number(form.cgpa) : null
      }
      await api.registerAndStore(payload)
      navigate('/dashboard')
    } catch (err) {
      setError(err.message)
    } finally {
      setBusy(false)
    }
  }

  return (
    <div className="auth-wrap">
      <div className="card">
        <h1>Create your account</h1>
        <p className="sub">Start tracking internships, offers and deadlines in minutes.</p>

        {error && <div className="alert alert-error">{error}</div>}

        <form className="form" onSubmit={submit}>
          <div className="field">
            <label htmlFor="name">Full name</label>
            <input id="name" name="name" value={form.name} onChange={change} required />
          </div>
          <div className="field">
            <label htmlFor="email">Email</label>
            <input id="email" name="email" type="email" value={form.email} onChange={change} required />
          </div>
          <div className="field">
            <label htmlFor="password">Password</label>
            <input
              id="password"
              name="password"
              type="password"
              minLength={6}
              value={form.password}
              onChange={change}
              required
            />
          </div>
          <div className="field">
            <label htmlFor="college">College</label>
            <input id="college" name="college" value={form.college} onChange={change} />
          </div>
          <div className="form-row">
            <div className="field">
              <label htmlFor="branch">Branch</label>
              <input id="branch" name="branch" value={form.branch} onChange={change} />
            </div>
            <div className="field">
              <label htmlFor="graduationYear">Graduation year</label>
              <input
                id="graduationYear"
                name="graduationYear"
                type="number"
                min="2024"
                max="2035"
                value={form.graduationYear}
                onChange={change}
              />
            </div>
          </div>
          <div className="form-row">
            <div className="field">
              <label htmlFor="cgpa">CGPA</label>
              <input
                id="cgpa"
                name="cgpa"
                type="number"
                step="0.01"
                min="0"
                max="10"
                value={form.cgpa}
                onChange={change}
              />
            </div>
            <div className="field">
              <label htmlFor="skills">Skills (comma separated)</label>
              <input id="skills" name="skills" value={form.skills} onChange={change} />
            </div>
          </div>
          <button className="btn btn-primary" type="submit" disabled={busy}>
            {busy ? 'Creating account…' : 'Create account'}
          </button>
        </form>

        <p className="auth-alt">
          Already registered? <Link to="/login">Sign in</Link>
        </p>
      </div>
    </div>
  )
}
