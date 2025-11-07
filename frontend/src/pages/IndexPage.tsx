import { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import styled from 'styled-components';


/** /api/me API 응답으로 받는 User 객체 타입 */
interface User {
  nickname: string;
  role: 'USER' | 'ADMIN';
  // (백엔드에서 더 많은 정보를 보낸다면 여기에 추가할 수 있습니다)
}

/** /api/summarize API 응답 타입 */
interface SummaryResponse {
  summary: string;
}


const PageWrapper = styled.div`
  max-width: 900px;
  margin: 2rem auto;
  padding: 2rem;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
  color: #333;
`;

const LoadingWrapper = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 80vh;
  font-size: 1.5rem;
  color: #888;
`;

const LoggedOutWrapper = styled(PageWrapper)`
  text-align: center;
  padding: 4rem 2rem;
  background: #f9f9f9;
  border-radius: 10px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);

  h2 {
    margin-top: 0;
    font-size: 1.8rem;
  }
  
  p {
    font-size: 1.1rem;
    color: #555;
    margin-bottom: 2rem;
  }
`;

const LoginLinkButton = styled(Link)`
  display: inline-block;
  background: #007bff;
  color: white;
  padding: 0.8rem 1.8rem;
  border-radius: 5px;
  text-decoration: none;
  font-size: 1.1rem;
  font-weight: 600;
  transition: background-color 0.2s ease;

  &:hover {
    background-color: #0056b3;
  }
`;

const Header = styled.header`
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-bottom: 1rem;
  border-bottom: 2px solid #f0f0f0;
  margin-bottom: 1.5rem;
`;

const WelcomeMessage = styled.p`
  font-size: 1.25rem;
  font-weight: 600;
  margin: 0;
  
  span {
    color: #007bff;
  }
`;

const LogoutButton = styled.button`
  background: #dc3545;
  color: white;
  border: none;
  padding: 0.6rem 1.2rem;
  border-radius: 5px;
  font-size: 0.9rem;
  font-weight: 600;
  cursor: pointer;
  transition: background-color 0.2s ease;

  &:hover {
    background-color: #c82333;
  }
`;

const Section = styled.section`
  margin-bottom: 2rem;
  padding: 1.5rem;
  background: #fdfdfd;
  border: 1px solid #eee;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.03);

  h2 {
    margin-top: 0;
    padding-bottom: 0.5rem;
    border-bottom: 1px solid #f0f0f0;
  }
`;

const NavLinks = styled.div`
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
`;

const StyledLink = styled(Link)`
  text-decoration: none;
  color: #0056b3;
  font-weight: 500;
  
  &:hover {
    text-decoration: underline;
  }
`;

const SummaryForm = styled.form`
  display: flex;
  flex-direction: column;
  gap: 1rem;
`;

const StyledTextarea = styled.textarea`
  width: 100%;
  min-height: 250px;
  padding: 1rem;
  font-size: 1rem;
  font-family: inherit;
  border: 1px solid #ccc;
  border-radius: 5px;
  resize: vertical;
  box-sizing: border-box;

  &:focus {
    outline: none;
    border-color: #007bff;
    box-shadow: 0 0 0 2px rgba(0, 123, 255, 0.25);
  }
`;

const SubmitButton = styled.button`
  align-self: flex-start;
  background: #28a745;
  color: white;
  padding: 0.8rem 1.5rem;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 1rem;
  font-weight: 600;
  transition: background-color 0.2s ease;

  &:disabled {
    background: #aaa;
    cursor: not-allowed;
  }
  
  &:not(:disabled):hover {
    background: #218838;
  }
`;

const ResultBox = styled.div`
  margin-top: 1.5rem;
  padding: 1.5rem;
  background: #f7f7f9;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  white-space: pre-wrap;
  line-height: 1.6;
  
  h3 {
    margin-top: 0;
    color: #333;
  }
`;

const ErrorMessage = styled.p`
  color: #dc3545;
  font-weight: 600;
  background: #fdf2f2;
  border: 1px solid #f5c6cb;
  padding: 1rem;
  border-radius: 5px;
`;

const LoadingMessage = styled.p`
  font-size: 1rem;
  color: #555;
  font-style: italic;
`;


function IndexPage() {
  const [article, setArticle] = useState<string>('');
  const [summary, setSummary] = useState<string>('');
  const [loadingSummary, setLoadingSummary] = useState<boolean>(false);
  const [error, setError] = useState<Error | null>(null); 
  const [user, setUser] = useState<User | null>(null); 
  const [loadingUser, setLoadingUser] = useState<boolean>(true);
  
  const navigate = useNavigate();

  useEffect(() => {
    const fetchUser = async () => {
      try {
        const response = await fetch('/api/me', {
          credentials: 'include',
        });
        if (response.ok) {
          const data = await response.json() as User;
          setUser(data);
        } else if (response.status === 401) {
          setUser(null);
        }
      } catch (err) {
        console.error('사용자 정보 로딩 실패:', err);
        setUser(null);
        if (err instanceof Error) {
          setError(err);
        } else {
          setError(new Error('알 수 없는 오류로 사용자 정보를 불러오지 못했습니다.'));
        }
      } finally {
        setLoadingUser(false);
      }
    };
    fetchUser();
  }, []);

  const handleLogout = async () => {
    try {
      const response = await fetch('/logout', {
        method: 'POST',
      });
      if (response.ok) {
        setUser(null);
        navigate('/login');
      }
    } catch (err) {
      console.error('로그아웃 실패:', err);
      if (err instanceof Error) {
        setError(err);
      } else {
        setError(new Error('로그아웃 중 알 수 없는 오류가 발생했습니다.'));
      }
    }
  };

  const handleSubmitSummary = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    if (article.trim() === '') {
      alert('요약할 기사를 입력하세요.');
      return;
    }
    setLoadingSummary(true);
    setError(null);
    setSummary('');

    try {
      const response = await fetch('/api/summarize', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ article }),
        credentials: 'include',
      });

      if (!response.ok) {
        throw new Error(`서버 오류: ${response.statusText} (코드: ${response.status})`);
      }

      const data = await response.json() as SummaryResponse;
      setSummary(data.summary);

    } catch (err) {
      if (err instanceof Error) {
        setError(err);
      } else {
        setError(new Error('요약 중 알 수 없는 오류가 발생했습니다.'));
      }
    } finally {
      setLoadingSummary(false);
    }
  };

  // 사용자 정보 로딩 중
  if (loadingUser) {
    return <LoadingWrapper>로딩 중...</LoadingWrapper>;
  }

  // 1. 비로그인 상태
  if (!user) {
    return (
      <LoggedOutWrapper>
        <h2>뉴스 요약 기능을 사용해보세요!</h2>
        <p>로그인하고 AI가 분석해주는 핵심 뉴스 요약 서비스를 이용해보세요.</p>
        <LoginLinkButton to="/login">로그인 페이지로 이동</LoginLinkButton>
      </LoggedOutWrapper>
    );
  }

  // 2. 로그인 상태
  return (
    <PageWrapper>
      <Header>
        <WelcomeMessage>
          <span>{user.nickname}</span>님 환영합니다.
        </WelcomeMessage>
        <LogoutButton onClick={handleLogout}>로그아웃</LogoutButton>
      </Header>

      <Section>
        <NavLinks>
          {user.role === 'ADMIN' && (
            <StyledLink to="/admin/users">
              <strong>[ADMIN]</strong> 사용자 목록
            </StyledLink>
          )}
          <StyledLink to="/users/me">마이페이지</StyledLink>
        </NavLinks>
      </Section>

      <Section>
        <h2>구독 플랜</h2>
        <NavLinks>
          <StyledLink to="/">News Summary</StyledLink>
          <StyledLink to="/subscription/hotIssue">Hot Issue</StyledLink>
          <StyledLink to="/subscription/domainInsight">Domain Insight</StyledLink>
        </NavLinks>
      </Section>
      
      <Section>
        <h2>뉴스 기사 요약</h2>
        <SummaryForm id="summarize-form" onSubmit={handleSubmitSummary}>
          <StyledTextarea
            id="article-text"
            rows={15} 
            placeholder="여기에 뉴스 기사 원문을 붙여넣으세요..."
            value={article}
            onChange={(e: React.ChangeEvent<HTMLTextAreaElement>) => setArticle(e.target.value)}
          ></StyledTextarea>
          <SubmitButton type="submit" disabled={loadingSummary}>
            {loadingSummary ? '요약 중...' : '요약하기'}
          </SubmitButton>
        </SummaryForm>

        {/* 요약 결과 표시 영역 */}
        {loadingSummary && <LoadingMessage>AI가 기사를 분석하고 있습니다...</LoadingMessage>}
        {error && <ErrorMessage><strong>오류 발생!</strong> {error.message}</ErrorMessage>}
        {summary && (
          <ResultBox>
            <h3>요약 결과</h3>
            <p>{summary}</p>
          </ResultBox>
        )}
      </Section>
    </PageWrapper>
  );
}

export default IndexPage;