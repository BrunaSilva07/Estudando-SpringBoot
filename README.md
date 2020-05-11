# Estudando-SpringBoot
Construção de uma APIs REST usando Spring Boot

# Módulo 2:
# Aula2 -  Melhorando desempenho com Spring Cache
-Utilizando cache: 
1° incluindo a dependência 'spring-boot-starter-cache' no pom.xml
2° incluindo a anotação 'EnableCaching' no main
3° incluindo a anotação 'Cacheable' no método 'lista'
4° incluindo o log do cache no properties com as declarações 'spring.jpa.properties.hibernate.show_sql=true' e 'spring.jpa.properties.hibernate.format_sql=true' com as finalidades de Mostrar no console o select da consulta e formatar em quebras de linhas

-Invalidando o cache: 
1° inclusão da anotação CacheEvict em cima dos métodos cadastrar, atualizar e deletar
2° A anotação CacheEvict deve receber os atributos 'value = ' com o nome dado ao cache do método 'lista' e 'allEntries =' com true para limpar todos os registros do cache

-Boas práticas no uso de cache: 
Sem implementação de código, debate sobre os prós e contras o uso do cache, evitar o uso de cache em tabelas que são modificadas com frequência para evitar queda na performance, utilizar em tabelas que raramente for altera.

# Aula3 -  Proteção com Spring Security
-Habilitando o Spring Security: 
1° incluindo a dependência 'spring-boot-starter-security' no pom.xml
2° criando a classe: SecurityConfigurations
3° incluindo as anotações 'SecurityConfigurations' e ‘Configuration’

-Liberando acesso aos endpoints públicos: 
1° incluindo os três métodos ‘configure’ da classe abstrata WebSecurityConfigurerAdapter, que foi extendida a classe SecurityConfigurations: AuthenticationManagerBuilder, HttpSecurity e WebSecurity

-Restringindo o acesso aos endpoints privados: 
1° Criando a classe entidade Perfil
2° implementando o ‘GrantedAuthority’ a classe Perfil
3° implementando o método ‘getAuthority’
4° criando as classes ‘AutenticacaoService’ e ‘UsuarioRepository’
5° implementando a classe ‘UserDetailsService’ e implementando o método ‘loadUserByUsername’.

