import { BrowserRouter, Routes, Route } from "react-router-dom";
import Layout from "./layout/Layout";
import Dashboard from "./pages/Dashboard";
import Customers from "./pages/Customers";
import Loans from "./pages/Loans";
import Evaluations from "./pages/Evaluations";
import Rules from "./pages/Rules";

export default function App() {
  return (
    <BrowserRouter>
      <Layout>
        <Routes>
          <Route path="/" element={<Dashboard />} />
          <Route path="/customers" element={<Customers />} />
          <Route path="/loans" element={<Loans />} />
          <Route path="/evaluations" element={<Evaluations />} />
          <Route path="/rules" element={<Rules />} />
        </Routes>
      </Layout>
    </BrowserRouter>
  );
}