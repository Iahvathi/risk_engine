import { useState } from "react";
import api from "../api/axios";

export default function Topbar() {
  const [tenant, setTenant] = useState("sbi-key");

  const handleTenantChange = (e) => {
    const selectedKey = e.target.value;
    setTenant(selectedKey);

    // Update axios default header dynamically
    api.defaults.headers["X-API-KEY"] = selectedKey;
  };

  return (
    <div style={{
      padding: "15px 30px",
      borderBottom: "1px solid var(--border)",
      background: "var(--bg-main)",
      display: "flex",
      justifyContent: "space-between"
    }}>
      <span>Loan Risk Decision Engine</span>

      <select value={tenant} onChange={handleTenantChange}>
        <option value="sbi-key">SBI Bank</option>
        <option value="hdfc-key">HDFC Bank</option>
      </select>
    </div>
  );
}