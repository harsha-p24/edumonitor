import { useState, useEffect, useCallback } from 'react';
import axiosClient from '../api/axiosClient';
import { useAuth } from '../context/useAuth';

const TABS = [
  { key: 'keyboard-activity', label: 'Keyboard Activity' },
  { key: 'mouse-activity', label: 'Mouse Activity' },
  { key: 'copy-paste-activity', label: 'Copy-Paste Activity' },
  { key: 'idle-students', label: 'Idle Students' },
];

export default function DashboardPage() {
  const [labId, setLabId] = useState(1);
  const [activeTab, setActiveTab] = useState('keyboard-activity');
  const [rows, setRows] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const { user, logout } = useAuth();

  const fetchData = useCallback(async () => {
    setLoading(true);
    setError('');
    try {
      const response = await axiosClient.get(`/dashboard/lab/${labId}/${activeTab}`);
      setRows(response.data);
    } catch (err) {
      const message = err.response?.data?.message || 'Failed to load dashboard data.';
      setError(message);
      setRows([]);
    } finally {
      setLoading(false);
    }
  }, [labId, activeTab]);

  useEffect(() => {
    // Intentional fetch-on-mount/dependency-change pattern (standard React
    // data-fetching idiom). This rule is overly strict about it here.
    // eslint-disable-next-line react-hooks/set-state-in-effect
    fetchData();
  }, [fetchData]);

  const currentTabLabel = TABS.find((t) => t.key === activeTab)?.label;

  return (
    <div style={{ maxWidth: 900, margin: '40px auto', fontFamily: 'sans-serif' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <h1>Teacher Dashboard</h1>
        <div>
          <span style={{ marginRight: 12 }}>{user?.name} ({user?.role})</span>
          <button onClick={logout}>Log Out</button>
        </div>
      </div>

      <div style={{ marginBottom: 16 }}>
        <label>Lab: </label>
        <select value={labId} onChange={(e) => setLabId(Number(e.target.value))}>
          <option value={1}>Lab 1</option>
          <option value={2}>Lab 2</option>
          <option value={3}>Lab 3</option>
          <option value={4}>Lab 4</option>
        </select>
        <button onClick={() => fetchData()} style={{ marginLeft: 12 }}>Refresh</button>
      </div>

      <div style={{ marginBottom: 16 }}>
        {TABS.map((tab) => (
          <button
            key={tab.key}
            onClick={() => setActiveTab(tab.key)}
            style={{
              marginRight: 8,
              padding: '8px 16px',
              fontWeight: activeTab === tab.key ? 'bold' : 'normal',
              backgroundColor: activeTab === tab.key ? '#333' : '#eee',
              color: activeTab === tab.key ? '#fff' : '#000',
              border: 'none',
              cursor: 'pointer',
            }}
          >
            {tab.label}
          </button>
        ))}
      </div>

      {loading && <p>Loading...</p>}
      {error && <p style={{ color: 'red' }}>{error}</p>}

      {!loading && !error && (
        <table style={{ width: '100%', borderCollapse: 'collapse' }}>
          <thead>
            <tr style={{ borderBottom: '2px solid #333', textAlign: 'left' }}>
              <th style={{ padding: 8 }}>Student Name</th>
              <th style={{ padding: 8 }}>USN</th>
              <th style={{ padding: 8 }}>PC</th>
              <th style={{ padding: 8 }}>{currentTabLabel}</th>
            </tr>
          </thead>
          <tbody>
            {rows.length === 0 ? (
              <tr>
                <td colSpan={4} style={{ padding: 8, textAlign: 'center', color: '#888' }}>
                  No active sessions in this lab.
                </td>
              </tr>
            ) : (
              rows.map((row) => (
                <tr key={row.sessionId} style={{ borderBottom: '1px solid #ddd' }}>
                  <td style={{ padding: 8 }}>{row.studentName}</td>
                  <td style={{ padding: 8 }}>{row.usn}</td>
                  <td style={{ padding: 8 }}>{row.pcLabel}</td>
                  <td style={{ padding: 8 }}>{row.metricValue}</td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      )}
    </div>
  );
}
