import { useEffect, useState } from "react";
import api from "../api/axios";

export default function Loans() {
  const [loans, setLoans] = useState([]);
  const [customers, setCustomers] = useState([]);
  const [form, setForm] = useState({
    customerId: "",
    requestedAmount: "",
    tenureMonths: "",
    employmentType: "",
    employerName: "",
    workExperienceYears: "",
    monthlyIncome: "",
    existingEmiAmount: "",
    loanPurpose: "",
    panNumber: ""
  });

  const fetchLoans = () => {
    api.get("/loans").then(res => setLoans(res.data.data));
  };

  const fetchCustomers = () => {
    api.get("/customers").then(res => setCustomers(res.data.data));
  };

  useEffect(() => {
    fetchLoans();
    fetchCustomers();
  }, []);

  const handleSubmit = (e) => {
    e.preventDefault();

    api.post("/loans", {
      customerId: Number(form.customerId),
      requestedAmount: Number(form.requestedAmount),
      tenureMonths: Number(form.tenureMonths),
      employmentType: form.employmentType,
      employerName: form.employerName,
      workExperienceYears: Number(form.workExperienceYears),
      monthlyIncome: Number(form.monthlyIncome),
      existingEmiAmount: Number(form.existingEmiAmount),
      loanPurpose: form.loanPurpose,
      panNumber: form.panNumber
    }).then(() => {
      fetchLoans();
      setForm({
        customerId: "",
        requestedAmount: "",
        tenureMonths: "",
        employmentType: "",
        employerName: "",
        workExperienceYears: "",
        monthlyIncome: "",
        existingEmiAmount: "",
        loanPurpose: "",
        panNumber: ""
      });
    });
  };

  const evaluateLoan = (loanId) => {
    api.post(`/risk-evaluation/evaluate/${loanId}`)
      .then(res => {
        alert("Evaluation ID: " + res.data.data.evaluationId);
        fetchLoans();
      });
  };

  return (
    <div>
      <h1>Loan Applications</h1>

      {/* CREATE LOAN */}
      <div className="card">
        <h3>Create Loan</h3>
        <form onSubmit={handleSubmit} style={{ display: "grid", gap: "10px" }}>

          <select
            value={form.customerId}
            onChange={e => setForm({ ...form, customerId: e.target.value })}
            required
          >
            <option value="">Select Customer</option>
            {customers.map(c => (
              <option key={c.id} value={c.id}>{c.name}</option>
            ))}
          </select>

          <input type="number" placeholder="Requested Amount"
            value={form.requestedAmount}
            onChange={e => setForm({ ...form, requestedAmount: e.target.value })}
            required />

          <input type="number" placeholder="Tenure (Months)"
            value={form.tenureMonths}
            onChange={e => setForm({ ...form, tenureMonths: e.target.value })}
            required />

          <input placeholder="Employment Type"
            value={form.employmentType}
            onChange={e => setForm({ ...form, employmentType: e.target.value })}
            required />

          <input placeholder="Employer Name"
            value={form.employerName}
            onChange={e => setForm({ ...form, employerName: e.target.value })}
            required />

          <input type="number" placeholder="Work Experience (Years)"
            value={form.workExperienceYears}
            onChange={e => setForm({ ...form, workExperienceYears: e.target.value })}
            required />

          <input type="number" placeholder="Monthly Income"
            value={form.monthlyIncome}
            onChange={e => setForm({ ...form, monthlyIncome: e.target.value })}
            required />

          <input type="number" placeholder="Existing EMI Amount"
            value={form.existingEmiAmount}
            onChange={e => setForm({ ...form, existingEmiAmount: e.target.value })}
            required />

          <input placeholder="Loan Purpose"
            value={form.loanPurpose}
            onChange={e => setForm({ ...form, loanPurpose: e.target.value })}
            required />

          <input placeholder="PAN Number"
            value={form.panNumber}
            onChange={e => setForm({ ...form, panNumber: e.target.value })}
            required />

          <button type="submit">Submit Loan</button>
        </form>
      </div>

      {/* LOAN LIST */}
      <div className="card">
        <h3>Loan List</h3>
        {loans.map(loan => (
          <div key={loan.id} style={{
            display: "flex",
            justifyContent: "space-between",
            padding: "10px 0",
            borderBottom: "1px solid var(--border)"
          }}>
            <span>
              ₹{loan.requestedAmount} — {loan.tenureMonths} months — {loan.status}
            </span>

            {(loan.status === "SUBMITTED" || loan.status === "UNDER_REVIEW") ? (
              <button onClick={() => evaluateLoan(loan.id)}>Evaluate</button>
            ) : (
              <span>Evaluated</span>
            )}
          </div>
        ))}
      </div>
    </div>
  );
}