import { NavLink } from "react-router-dom";

export default function Sidebar() {
  const linkStyle = ({ isActive }) => ({
    display: "block",
    padding: "12px 0",
    color: isActive ? "white" : "var(--text-muted)",
    textDecoration: "none"
  });

  return (
    <div style={{
      width: "240px",
      background: "var(--bg-card)",
      minHeight: "100vh",
      padding: "30px 20px",
      borderRight: "1px solid var(--border)"
    }}>
      <h2 style={{ color: "var(--accent)", marginBottom: "40px" }}>
        Risk Engine
      </h2>

      <nav>
        <NavLink to="/" style={linkStyle}>Dashboard</NavLink>
        <NavLink to="/customers" style={linkStyle}>Customers</NavLink>
        <NavLink to="/loans" style={linkStyle}>Loans</NavLink>
    
        <NavLink to="/rules" style={linkStyle}>Rules</NavLink>
        <NavLink to="/evaluations" style={linkStyle}>Evaluations</NavLink>
      </nav>
    </div>
  );
}