import { AppRouter } from "./router/AppRouter";
import { Toaster } from 'react-hot-toast';

function App() {
  return (
    <>
      <AppRouter />
      <Toaster
        position="top-right"
        toastOptions={{
          duration: 3500,
          style: {
            background: "#1f2937", 
            color: "#f3f4f6",      
            border: "1px solid #374151", 
            borderRadius: "0.5rem", 
            padding: "0.75rem 1rem",
            fontSize: "0.875rem",
            boxShadow: "0 4px 12px rgba(0,0,0,0.25)",
          },
          success: {
            iconTheme: {
              primary: "#615fff",
              secondary: "#1f2937",
            },
            style: {
              background: "#1f2937",
              borderColor: "#615fff",
              color: "#fee2e2",
            },
          },
          error: {
            iconTheme: {
              primary: "#ef4444",
              secondary: "#1f2937",
            },
            style: {
              background: "#1f2937",
              borderColor: "#ef4444",
              color: "#fee2e2",
            },
          },
        }}
      />

    </>
  )
}

export default App
