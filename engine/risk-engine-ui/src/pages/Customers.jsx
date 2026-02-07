import { useEffect, useState } from "react";
import api from "../api/axios";

export default function Customers() {
  const [customers, setCustomers] = useState([]);
  const [form, setForm] = useState({
    fullName: "",
    age: "",
    annualIncome: "",
    status: "ACTIVE",
    nationalId: ""
  });

  const fetchCustomers = () => {
    api.get("/customers").then(res => {
      setCustomers(res.data.data);
    });
  };

  useEffect(() => {
    fetchCustomers();
  }, []);

  const handleSubmit = (e) => {
    e.preventDefault();
    api.post("/customers", form).then(() => {
      fetchCustomers();
      setForm({ fullName: "", age: "", annualIncome: "", status: "ACTIVE", nationalId: "" });
    });
  };

  return (
    <div>
      <h1>Customers</h1>

      <div className="card">
        <h3>Create Customer</h3>
        <form onSubmit={handleSubmit} style={{ display: "grid", gap: "10px" }}>
          <input placeholder="Full Name" value={form.fullName}
            onChange={e => setForm({ ...form, fullName: e.target.value })} />

          <input placeholder="Age" type="number" value={form.age}
            onChange={e => setForm({ ...form, age: e.target.value })} />

          <input placeholder="Annual Income" type="number" value={form.annualIncome}
            onChange={e => setForm({ ...form, annualIncome: e.target.value })} />

          <select value={form.status}
            onChange={e => setForm({ ...form, status: e.target.value })}>
            <option>ACTIVE</option>
            <option>INACTIVE</option>
            <option>BLACKLISTED</option>
          </select>

          <input placeholder="National ID" value={form.nationalId}
            onChange={e => setForm({ ...form, nationalId: e.target.value })} />

          <button type="submit">Create</button>
        </form>
      </div>

      <div className="card">
        <h3>Customer List</h3>
        {customers.map(c => (
          <p key={c.id}>{c.name} — {c.status}</p>
        ))}
      </div>
    </div>
  );
}