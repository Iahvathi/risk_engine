export default function StatCard({ title, value }) {
  return (
    <div style={{
      flex: 1,
      background: "var(--bg-card)",
      padding: "25px",
      borderRadius: "12px",
      border: "1px solid var(--border)"
    }}>
      <p style={{ color: "var(--text-muted)" }}>{title}</p>
      <h2 style={{ fontSize: "28px" }}>{value}</h2>
    </div>
  );
}