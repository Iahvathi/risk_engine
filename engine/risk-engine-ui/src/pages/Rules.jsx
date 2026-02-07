import { useEffect, useState } from "react";
import api from "../api/axios";

export default function Rules() {
  const [rules, setRules] = useState([]);
  const [loading, setLoading] = useState(true);

  const fetchRules = () => {
    api.get("/rules")
      .then(res => {
        setRules(res.data.data || []);
      })
      .catch(err => {
        console.error("Failed to load rules", err);
        alert("Failed to load rules");
      })
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    fetchRules();
  }, []);

  return (
    <div>
      <h1>Rules Viewer</h1>

      <div className="card">
        {loading ? (
          <p>Loading rules...</p>
        ) : rules.length === 0 ? (
          <p>No rules found.</p>
        ) : (
          <table style={{ width: "100%", borderCollapse: "collapse" }}>
            <thead>
              <tr style={{ textAlign: "left", borderBottom: "1px solid var(--border)" }}>
                <th>Name</th>
                <th>Condition</th>
                <th>Action</th>
                <th>Priority</th>
                <th>Type</th>
                <th>Status</th>
              </tr>
            </thead>
            <tbody>
              {rules.map(rule => (
                <tr key={rule.id} style={{ borderBottom: "1px solid var(--border)" }}>
                  <td>{rule.name}</td>
                  <td>{rule.conditionExpression}</td>
                  <td>{rule.actionValue}</td>
                  <td>{rule.priority}</td>
                  <td>{rule.ruleType}</td>
                  <td>
                    {rule.enabled ? (
                      <span style={{ color: "limegreen" }}>Enabled</span>
                    ) : (
                      <span style={{ color: "gray" }}>Disabled</span>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
}