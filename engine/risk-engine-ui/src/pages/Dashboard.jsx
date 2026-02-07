import { useEffect, useState } from "react";
import api from "../api/axios";

export default function Dashboard() {
  const [stats, setStats] = useState({
    customers: 0,
    loans: 0,
    evaluations: 0
  });

  useEffect(() => {
    const fetchStats = async () => {
      try {
        const [customersRes, loansRes] = await Promise.all([
          api.get("/customers"),
          api.get("/loans"),
        ]);

        setStats({
          customers: customersRes.data.data.length,
          loans: loansRes.data.data.length,
          evaluations: 0 // update when endpoint exists
        });

      } catch (err) {
        console.error("Failed to load dashboard stats", err);
      }
    };

    fetchStats();
  }, []);

  return (
    <div>
      <h1>Robust Decision Making</h1>
      <p>Real-time automated loan risk evaluation system</p>

      <div style={{ display: "flex", gap: "20px", marginTop: "30px" }}>
        <StatCard title="Customers" value={stats.customers} />
        <StatCard title="Loans" value={stats.loans} />
        <StatCard title="Evaluations" value={stats.evaluations} />
      </div>
    </div>
  );
}

function StatCard({ title, value }) {
  return (
    <div style={{
      background: "rgba(255,255,255,0.05)",
      padding: "25px",
      borderRadius: "12px",
      minWidth: "200px"
    }}>
      <h3>{title}</h3>
      <h2>{value}</h2>
    </div>
  );
}