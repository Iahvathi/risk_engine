import { useState } from "react";
import api from "../api/axios";

export default function Evaluations() {
  const [evaluationId, setEvaluationId] = useState("");
  const [evaluation, setEvaluation] = useState(null);
  const [audits, setAudits] = useState([]);

  const fetchEvaluation = () => {
    api.get(`/risk-evaluations/${evaluationId}/audits`)
      .then(res => setAudits(res.data.data));
  };

  return (
    <div>
      <h1>Risk Evaluation Audit Viewer</h1>

      <div className="card">
        <h3>Enter Evaluation ID</h3>
        <input
          placeholder="Evaluation ID"
          value={evaluationId}
          onChange={e => setEvaluationId(e.target.value)}
        />
        <button onClick={fetchEvaluation}>Load Audit Trail</button>
      </div>

      {audits.length > 0 && (
        <div className="card">
          <h3>Audit Trail</h3>
          {audits.map((a, index) => (
            <div key={index} style={{
              padding: "10px",
              borderBottom: "1px solid var(--border)"
            }}>
              <p><b>Rule:</b> {a.ruleName || "Fallback Decision"}</p>
              <p><b>Matched:</b> {a.matched ? "Yes" : "No"}</p>
              <p><b>Action Taken:</b> {a.actionTaken}</p>
              <p><b>Order:</b> {a.executionOrder}</p>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}