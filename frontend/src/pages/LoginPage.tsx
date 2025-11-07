import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import styled from 'styled-components';


const PageContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #f0f2f5;
`;

const LoginBox = styled.div`
  width: 400px;
  padding: 2.5rem;
  background: #ffffff;
  border-radius: 10px;
  box-shadow: 0 8px 24px rgba(149, 157, 165, 0.2);
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
`;

const Title = styled.h1`
  font-size: 2rem;
  font-weight: 700;
  text-align: center;
  color: #333;
  margin: 0;
`;

const StyledForm = styled.form`
  display: flex;
  flex-direction: column;
  gap: 1rem;
`;

const InputGroup = styled.div`
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
`;

const StyledLabel = styled.label`
  font-size: 0.9rem;
  font-weight: 600;
  color: #555;
`;

const StyledInput = styled.input`
  width: 100%;
  padding: 0.8rem 1rem;
  font-size: 1rem;
  border: 1px solid #ddd;
  border-radius: 5px;
  box-sizing: border-box;

  &:focus {
    outline: none;
    border-color: #007bff;
    box-shadow: 0 0 0 2px rgba(0, 123, 255, 0.25);
  }
`;

const ButtonContainer = styled.div`
  display: flex;
  gap: 0.5rem;
`;

const BaseButton = styled.input`
  flex: 1;
  padding: 0.8rem 1rem;
  font-size: 1rem;
  font-weight: 600;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.2s ease;
`;

const PrimaryButton = styled(BaseButton)`
  background-color: #007bff;
  color: white;

  &:hover {
    background-color: #0056b3;
  }
`;

const SecondaryButton = styled(BaseButton)`
  background-color: #6c757d;
  color: white;

  &:hover {
    background-color: #5a6268;
  }
`;

const SocialLoginSection = styled.div`
  margin-top: 1rem;
  border-top: 1px solid #eee;
  padding-top: 1.5rem;
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
  text-align: center;
`;

const SocialLoginLink = styled.a`
  display: block;
  padding: 0.75rem 1rem;
  border-radius: 5px;
  text-decoration: none;
  font-weight: 500;
  transition: all 0.2s ease;

  &[href*="google"] {
    background-color: #f8f8f8;
    border: 1px solid #ddd;
    color: #444;
    &:hover {
      background-color: #f1f1f1;
      border-color: #ccc;
    }
  }

  &[href*="naver"] {
    background-color: #03c75a;
    border: 1px solid #02b350;
    color: white;
    &:hover {
      background-color: #02b350;
    }
  }
`;


function LoginPage() {
  const [userid, setUserid] = useState<string>('');
  const [password, setPassword] = useState<string>('');
  const navigate = useNavigate();

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const formData = new URLSearchParams();
    formData.append('userid', userid);
    formData.append('password', password);

    try {
      const response = await fetch('/auth/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: formData,
        credentials: 'include',
      });

      if (response.ok) {
        alert('로그인 성공!');
        navigate('/');
      } else {
        alert('아이디 또는 비밀번호가 틀렸습니다.');
      }
    } catch (error) {
      console.error('로그인 요청 중 오류 발생:', error);
      if (error instanceof Error) {
        alert(`로그인 중 문제가 발생했습니다: ${error.message}`);
      } else {
        alert('로그인 중 알 수 없는 문제가 발생했습니다.');
      }
    }
  };

  return (
    <PageContainer>
      <LoginBox>
        <Title>로그인</Title>
        
        <StyledForm onSubmit={handleSubmit}>
          <InputGroup>
            <StyledLabel htmlFor="userid">아이디</StyledLabel>
            <StyledInput
              id="userid"
              type="text"
              value={userid}
              onChange={(e: React.ChangeEvent<HTMLInputElement>) => setUserid(e.target.value)}
              required
            />
          </InputGroup>
          <InputGroup>
            <StyledLabel htmlFor="password">비밀번호</StyledLabel>
            <StyledInput
              id="password"
              type="password"
              value={password}
              onChange={(e: React.ChangeEvent<HTMLInputElement>) => setPassword(e.target.value)}
              required
            />
          </InputGroup>
          
          <ButtonContainer>
            <PrimaryButton type="submit" value="로그인" />
            <SecondaryButton type="button" value="회원가입" onClick={() => navigate('/signup')} />
          </ButtonContainer>
        </StyledForm>

        <SocialLoginSection>
          <h3 style={{ margin: '0 0 0.5rem 0', fontSize: '0.9rem', color: '#666', fontWeight: 500 }}>소셜 계정으로 로그인</h3>
          <SocialLoginLink href="/oauth2/authorization/google">Google로 로그인</SocialLoginLink>
          <SocialLoginLink href="/oauth2/authorization/naver">Naver로 로그인</SocialLoginLink>
        </SocialLoginSection>
      </LoginBox>
    </PageContainer>
  );
}

export default LoginPage;